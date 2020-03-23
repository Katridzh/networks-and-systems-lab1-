package com.company;

import java.util.LinkedList;


public class Polynom {
    private LinkedList<Integer> coeff;
    private int deg;

    Polynom(LinkedList<Integer> c, int d) {
        coeff = new LinkedList<>();
        coeff.addAll(c);
        deg = d;

    }

    Polynom(int d, int val) {
        deg = d;
        coeff = new LinkedList<>();
        coeff.add(val);
        if (d > 0) {
            for (int i = 1; i <= deg; i++) {
                coeff.add(0);
            }
        }
    }

    public int getDeg() {
        return deg;
    }

    public int getCoeff(int i) {
        return coeff.get(i);
    }

    public LinkedList<Integer> getCoeff() {
        return coeff;
    }

    public void setCoeff(int i, int val) {
        coeff.set(i, val);
    }

    public Polynom mult(Polynom b) {
        Polynom result = new Polynom(this.deg + b.getDeg(), 0);
        for (int i = this.deg; i >= 0; i--)
            for (int j = b.deg; j >= 0; j--) {
                if (this.getCoeff(this.deg - i) != 0 && b.getCoeff(b.deg - j) != 0)
                    result.setCoeff(this.deg - i + b.deg - j, result.getCoeff(this.deg - i + b.deg - j) + this.coeff.get(this.deg - i) * b.coeff.get(b.deg - j));
                if (result.getCoeff(this.deg - i + b.deg - j) == 2)
                    result.setCoeff(this.deg - i + b.deg - j, 0);
            }
        return result;
    }

    public Polynom xor(Polynom b) {
        Polynom result = new Polynom(this.deg, 0);
        for (int i = 0; i <= this.deg; i++)
            result.setCoeff(i, (this.getCoeff(i) + b.getCoeff(i)) % 2);
        return result;
    }

    public boolean isnull() {
        int count = 0;
        int ind = 0;
        while (ind < coeff.size()) {
            if (coeff.get(ind) != 0) {
                count++;
                break;
            }
            ind++;
        }
        if (count != 0)
            return false;
        else
            return true;
    }

    public Polynom sum(Polynom b) {
        Polynom result = new Polynom(this.coeff, this.deg);
        if (this.deg > b.deg)
            for (int i = 0; i <= b.deg; i++)
                result.setCoeff(i + this.deg - b.deg, (this.getCoeff(i + this.deg - b.deg) + b.getCoeff(i)) % 2);
        else
            for (int i = 0; i <= this.deg; i++)
                result.setCoeff(i + b.deg - this.deg, (this.getCoeff(i) + b.getCoeff(i + b.deg - this.deg)) % 2);
        int new_deg = 0;
        while (result.getCoeff().size() - 1 > 0 & result.getCoeff(0) == 0) {
            new_deg++;
            result.getCoeff().remove(0);
        }
        result = new Polynom(result.getCoeff(), result.deg - new_deg);
        return result;
    }

    public Polynom mod(Polynom b) {
        Polynom result_t = new Polynom(this.coeff, this.deg);
        while (result_t.deg >= b.deg) {
            Polynom temp = b.mult(new Polynom(result_t.deg - b.deg, 1));
            for (int i = 0; i <= temp.deg; i++) {
                if (result_t.getCoeff(i) - temp.getCoeff(i) >= 0)
                    result_t.setCoeff(i, result_t.getCoeff(i) - temp.getCoeff(i));
                else
                    result_t.setCoeff(i, 2 - result_t.getCoeff(i) - temp.getCoeff(i));
            }
            int new_deg = 0;
            while (result_t.getCoeff().size() - 1 > 0 & result_t.getCoeff(0) == 0) {
                result_t.deg--;
                result_t.getCoeff().remove(0);
            }
            result_t = new Polynom(result_t.getCoeff(), result_t.deg - new_deg);
        }
        return result_t;
    }

    public String toString() {
        StringBuilder strBuild = new StringBuilder();
        if (deg == 0)
            strBuild.append(coeff.getFirst());
        else {
            if (coeff.getFirst() == 1)
                strBuild.append("x^" + deg);
            else
                strBuild.append(coeff.getFirst() + "x^" + deg);
            for (int i = 1; i < deg; i++) {
                if (coeff.get(i) != 0)
                    if (coeff.get(i) == 1)
                        strBuild.append(" + " + "x^" + (deg - i));
                    else
                        strBuild.append(" + " + coeff.get(i) + "x^" + (deg - i));
            }
            if (coeff.getLast() != 0)
                strBuild.append(" + " + coeff.getLast());
        }
        return strBuild.toString();
    }
}
