//----------------------Request.java----------------------

package academy.pocu.comp2500.lab10;

import academy.pocu.comp2500.lab10.pocuflix.User;

import java.time.OffsetDateTime;

public class Request {
    private final String title;
    private User user;

    public Request(String title) {
        this.title = title;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return false;
        }

        if (obj == null || !(obj instanceof Request) || this.hashCode() != obj.hashCode()) {
            return false;
        }

        Request other = (Request) obj;
        return this.user != null ? this.title.equals(other.title) && this.user.equals(other.user) : this.title.equals(other.title);
    }

    @Override
    public int hashCode() {
        return this.user != null ? this.title.hashCode() * this.user.hashCode() : this.title.hashCode();
    }
}


//----------------------MovieStore.java----------------------

package academy.pocu.comp2500.lab10;

import academy.pocu.comp2500.lab10.pocuflix.Movie;
import academy.pocu.comp2500.lab10.pocuflix.NotFoundResult;
import academy.pocu.comp2500.lab10.pocuflix.OkResult;
import academy.pocu.comp2500.lab10.pocuflix.ResultBase;

import java.util.ArrayList;

public class MovieStore implements IRequestHandler {
    private final ArrayList<Movie> movies;

    public MovieStore() {
        this.movies = new ArrayList<>();
    }

    public void add(Movie movie) {
        if (!this.movies.contains(movie)) {
            this.movies.add(movie);
        }
    }

    public boolean remove(int index) {
        if (index > this.movies.size() - 1) {
            return false;
        }

        this.movies.remove(index);
        return true;
    }

    @Override
    public ResultBase handle(Request request) { // movie with same title == same movie
        String requestedTitle = request.getTitle();

        for (Movie movie : this.movies) {
            if (movie.getTitle().equals(requestedTitle)) {
                return new OkResult(movie);
            }
        }

        return new NotFoundResult();
    }
}


//----------------------MaintenanceMiddleware.java----------------------

package academy.pocu.comp2500.lab10;

import academy.pocu.comp2500.lab10.pocuflix.ResultBase;

import java.time.OffsetDateTime;

public class MaintenanceMiddleware implements IRequestHandler {
    private final IRequestHandler nextHandler;
    private final OffsetDateTime maintenanceStartTime;
    private final OffsetDateTime maintenanceEndTime;

    public MaintenanceMiddleware(IRequestHandler nextHandler, OffsetDateTime maintenanceStartTime) {
        this.nextHandler = nextHandler;
        this.maintenanceStartTime = maintenanceStartTime;
        this.maintenanceEndTime = this.maintenanceStartTime.plusHours(1);
    }

    public ResultBase handle(Request request) {
        OffsetDateTime requestedTime = OffsetDateTime.now();

        if ((requestedTime.isEqual(this.maintenanceStartTime) || requestedTime.isAfter(this.maintenanceStartTime))
                && (requestedTime.isEqual(this.maintenanceEndTime) || requestedTime.isBefore(this.maintenanceEndTime))) {
            return new ServiceUnavailableResult(this.maintenanceStartTime, this.maintenanceEndTime);
        }

        return this.nextHandler.handle(request);
    }
}


//----------------------ServiceUnavailableResult.java----------------------

package academy.pocu.comp2500.lab10;

import academy.pocu.comp2500.lab10.pocuflix.ResultBase;
import academy.pocu.comp2500.lab10.pocuflix.ResultCode;

import java.time.OffsetDateTime;

public class ServiceUnavailableResult extends ResultBase {
    private final OffsetDateTime startDateTime;
    private final OffsetDateTime endDateTime;

    public ServiceUnavailableResult(OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        super(ResultCode.SERVICE_UNAVAILABLE);

        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public OffsetDateTime getStartDateTime() {
        return this.startDateTime;
    }

    public OffsetDateTime getEndDateTime() {
        return this.endDateTime;
    }
}


//----------------------AuthorizationMiddleware.java----------------------

package academy.pocu.comp2500.lab10;

import academy.pocu.comp2500.lab10.pocuflix.ResultBase;
import academy.pocu.comp2500.lab10.pocuflix.User;

import java.util.HashSet;

public class AuthorizationMiddleware implements IRequestHandler {
    private final IRequestHandler nextHandler;
    private final HashSet<User> authorizedUsers;

    public AuthorizationMiddleware(IRequestHandler nextHandler, HashSet<User> authorizedUsers) {
        this.nextHandler = nextHandler;
        this.authorizedUsers = authorizedUsers;
    }

    public ResultBase handle(Request request) {
        if (!this.authorizedUsers.contains(request.getUser())) {
            return new UnauthorizedResult();
        }

        return this.nextHandler.handle(request);
    }
}


//----------------------UnauthorizedResult.java----------------------

package academy.pocu.comp2500.lab10;

import academy.pocu.comp2500.lab10.pocuflix.ResultBase;
import academy.pocu.comp2500.lab10.pocuflix.ResultCode;

public class UnauthorizedResult extends ResultBase {

    public UnauthorizedResult() {
        super(ResultCode.UNAUTHORIZED);
    }

    public String getErrorMessage() {
        return "Unauthorized access";
    }
}


//----------------------CacheMiddleware.java----------------------

package academy.pocu.comp2500.lab10;

import academy.pocu.comp2500.lab10.pocuflix.ResultBase;
import academy.pocu.comp2500.lab10.pocuflix.ResultCode;

import java.util.HashMap;

public class CacheMiddleware implements IRequestHandler {
    private final IRequestHandler nextHandler;
    private final int expiryCount;
    private final HashMap<Request, Integer> expiryCountByRequest;

    public CacheMiddleware(IRequestHandler nextHandler, final int expiryCount) {
        this.expiryCount = expiryCount;
        this.nextHandler = nextHandler;

        this.expiryCountByRequest = new HashMap<>();
    }

    public ResultBase handle(Request request) {
        if (this.expiryCountByRequest.containsKey(request) && this.expiryCountByRequest.get(request) > 1) {
            int expiryCount = this.expiryCountByRequest.get(request);
            this.expiryCountByRequest.put(request, expiryCount - 1);

            return new CachedResult(expiryCount - 1);
        }

        ResultBase resultBase = this.nextHandler.handle(request);
        ResultValidator validator = new ResultValidator(resultBase);
        if (validator.isValid(ResultCode.OK)) {
            this.expiryCountByRequest.put(request, this.expiryCount);
        }

        return this.nextHandler.handle(request);
    }
}


//----------------------CachedResult.java----------------------

package academy.pocu.comp2500.lab10;

import academy.pocu.comp2500.lab10.pocuflix.ResultBase;
import academy.pocu.comp2500.lab10.pocuflix.ResultCode;

public class CachedResult extends ResultBase {
    private int expiryCount;

    public CachedResult(int expiryCount) {
        super(ResultCode.NOT_MODIFIED);

        this.expiryCount = expiryCount;
    }

    public int getExpiryCount() {
        return this.expiryCount;
    }
}


//----------------------ResultValidator.java----------------------

package academy.pocu.comp2500.lab10;

import academy.pocu.comp2500.lab10.pocuflix.NotFoundResult;
import academy.pocu.comp2500.lab10.pocuflix.OkResult;
import academy.pocu.comp2500.lab10.pocuflix.ResultBase;
import academy.pocu.comp2500.lab10.pocuflix.ResultCode;

public class ResultValidator {
    private ResultBase resultBase;
    public ResultValidator(ResultBase resultBase) {
        assert (resultBase != null) : "resultBase cannot be null";
        assert (resultBase.getCode() != null) : "resultBase.getCode() cannot be null";

        this.resultBase = resultBase;
    }

    public boolean isValid(ResultCode resultCode) {
        assert (resultCode != null) : "resultCode cannot be null";

        switch (resultCode) {
            case OK:
                return this.resultBase instanceof OkResult
                        && this.resultBase.getCode() == resultCode;
            case NOT_FOUND:
                return this.resultBase instanceof NotFoundResult
                        && this.resultBase.getCode() == resultCode;
            case SERVICE_UNAVAILABLE:
                return this.resultBase instanceof ServiceUnavailableResult
                        && this.resultBase.getCode() == resultCode;
            case UNAUTHORIZED:
                return this.resultBase instanceof UnauthorizedResult
                        && this.resultBase.getCode() == resultCode;
            case NOT_MODIFIED:
                return this.resultBase instanceof CachedResult
                        && this.resultBase.getCode() == resultCode;
            default:
                assert (false) : "undefined resultCode";
                return false;
        }
    }
}


