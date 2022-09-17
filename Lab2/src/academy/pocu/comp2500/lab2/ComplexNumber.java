package academy.pocu.comp2500.lab2;

//쓸모 없는 주석 지울 것
public class ComplexNumber {
    public double real;
    public double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber(double real) {
        this(real, 0.0);
    }

    public ComplexNumber() {
        this(0.0, 0.0);
    }

    public boolean isReal() {
        return this.imaginary == 0 ? true : false;
    }

    public boolean isImaginary() {
        return this.real == 0 ? true : false;
    }

    public ComplexNumber getConjugate() {
        return new ComplexNumber(this.real, this.imaginary * -1);
    }

    public ComplexNumber add(final ComplexNumber num) {
        final double real = this.real + num.real;
        final double imaginary = this.imaginary + num.imaginary ;

        return new ComplexNumber(real, imaginary);
    }

    public ComplexNumber subtract(final ComplexNumber num) {
        final double real = this.real - num.real;
        final double imaginary = this.imaginary - num.imaginary ;

        return new ComplexNumber(real, imaginary);
    }

    public ComplexNumber multiply(final ComplexNumber num) {
     /*
     (a + bi)(c + di) = ac + adi + bci + bdi^2   // i^2 = -1 임
                 = (ac - bd) + (ad + bc)i
      */
        final double real = (this.real * num.real) - (this.imaginary * num.imaginary);
        final double imaginary = (this.real * num.imaginary) + (this.imaginary * num.real);

        return new ComplexNumber(real, imaginary);
    }

    public ComplexNumber divide(ComplexNumber num) {
        /*
        (a + bi) / (c + di) = [(a + bi) / (c + di)] * [(c - di) / (c - di)]
                    = (a + bi)(c - di) / (c^2 + d^2)
         */

        ComplexNumber numberator = this.multiply(num.getConjugate());

        double denominator = (num.real * num.real) + (num.imaginary * num.imaginary);

        if (denominator == 0) {
            return null;
        }

        return new ComplexNumber(numberator.real / denominator, numberator.imaginary / denominator);
    }
}

/*

2.4 getConjugate() 메서드를 구현한다
getConjugate() 메서드는 인자를 받지 않습니다.
현재 복소수의 켤레 복소수를 반환합니다.
ComplexNumber num1 = new ComplexNumber(2.1, -1.1);

ComplexNumber conjugate = num1.getConjugate();
// conjugate: 2.1 + 1.1i

2.5 add() 메서드를 구현한다
add() 메서드는 다음의 인자를 받습니다.
더할 복소수: ComplexNumber num
현재 인스턴스와 num을 합한 결과를 반환합니다.
ComplexNumber num1 = new ComplexNumber(0.5, -1.5);
ComplexNumber num2 = new ComplexNumber(-2.5, 4.0);

ComplexNumber sum = num1.add(num2); // sum: -2.0 + 2.5i

2.6 subtract() 메서드를 구현한다
subtract() 메서드는 다음의 인자를 받습니다.
뺄 복소수: ComplexNumber num
현재 인스턴스에서 num을 뺀 결과를 반환합니다.
ComplexNumber num1 = new ComplexNumber(0.5, -1.5);
ComplexNumber num2 = new ComplexNumber(-2.5, 4.0);

ComplexNumber diff = num1.subtract(num2); // diff: 3.0 -5.5i

2.7 multiply() 메서드를 구현한다
multiply() 메서드는 다음의 인자를 받습니다.
곱할 복소수: ComplexNumber num
현재 인스턴스와 num의 곱을 반환합니다.
ComplexNumber num1 = new ComplexNumber(0.5, -1.5);
ComplexNumber num2 = new ComplexNumber(-2.5, 4.0);

ComplexNumber product = num1.multiply(num2); // product: 4.75 + 5.75i

2.8 divide() 메서드를 구현한다
divide() 메서드는 다음의 인자를 받습니다.
나눗셈에 분모로 사용할 복소수: ComplexNumber num
현재 인스턴스를 num으로 나눈 결과를 반환합니다.
ComplexNumber num1 = new ComplexNumber(0.5, -1.5);
ComplexNumber num2 = new ComplexNumber(-2.5, 4.0);

ComplexNumber quotient = num1.divide(num2); // quotient: -0.32584 + 0.07865i
 */