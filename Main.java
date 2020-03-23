package com.company;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static java.lang.Math.pow;

public class Main {

    public static void main(String[] args) {
        LinkedList<Integer> coeff = new LinkedList<>();
        coeff.add(1);
        coeff.add(0);
        coeff.add(1);
        coeff.add(1);
        int deg = 3;
        Polynom g = new Polynom(coeff, deg);
        int k = 4;
        double eps = 0.005;
        int N=(int)(9/(4*pow(eps,2)));
        System.out.println(N);
        Map<Integer, Double> prob = new HashMap<>();
        /*LinkedList<Polynom> list_a = new LinkedList<>();
        for(int i=0;i<pow(2,k-1);i++) {
            int num=i;
            coeff = new LinkedList<>();
            coeff.add(1);
            for(int j=1; j<k;j++) {
                coeff.add(num%2);
                num/=2;
            }
            Polynom m = new Polynom(coeff, k-1);
            Polynom m_p = m.mult(new Polynom(deg, 1));
            list_a.add(m_p.sum(m_p.mod(g)));
        }*/
        for (int l = k - 2; l < k + 4; l+=1) {
            int count =0;
            int error = 0;
            LinkedList<Polynom> list_a = new LinkedList<>();
            for(int i=0;i<pow(2,l-1);i++) {
                int num=i;
                coeff = new LinkedList<>();
                coeff.add(1);
                for(int j=1; j<l;j++) {
                    coeff.add(num%2);
                    num/=2;
                }
                Polynom m = new Polynom(coeff, l-1);
                Polynom m_p = m.mult(new Polynom(deg, 1));
                list_a.add(m_p.sum(m_p.mod(g)));
            }
            while (count != N) {
                Polynom a =list_a.get((int) ( Math.random() * list_a.size()));
                coeff = new LinkedList<>();
                for (int i=0;i<=l-1+deg;i++)
                    if((Math.random() ) <= 0.3)
                        coeff.add(1);
                    else
                        coeff.add(0);
                Polynom e = new Polynom(coeff, l-1+deg);
                Polynom b = a.xor(e);
                if (b.mod(g).isnull() && !e.isnull())
                    error++;
                count++;
            }
            prob.put(l, (double)error / count);
        }
        System.out.println(prob);
        try ( PrintWriter writer = new PrintWriter(new File("result.csv")) ) {
            for ( Map.Entry<Integer, Double> entry : prob.entrySet() ) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
