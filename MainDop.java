package com.company;

import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Math.pow;

public class MainDop {
    public static void main(String[] args) {
        for (int deg=3; deg<=4; deg++) {
            LinkedList<Integer> coeff = new LinkedList<>();//�������� ������������ ������������ ����������
            if(deg == 3) { // x^3 + x + 1
                coeff.add(1);
                coeff.add(0);
                coeff.add(1);
                coeff.add(1);
            }
            else { // x^4 + x + 1
                coeff.add(1);
                coeff.add(0);
                coeff.add(0);
                coeff.add(1);
                coeff.add(1);
            }
            Polynom g = new Polynom(coeff, deg);//�������� ���������
            int k = 4;
            double eps = 0.005;
            int N = (int) (9 / (4 * pow(eps, 2))); // �������������� ���������� ���������
            LinkedList<Polynom> list_a = new LinkedList<>(); // ������ ��������� ������������ ���������
            for (int i = 0; i < pow(2, k - 1); i++) { // ����������� ��� ��������� ���������
                int num = i;
                coeff = new LinkedList<>();
                coeff.add(1);
                for (int j = 1; j < k; j++) {
                    coeff.add(num % 2);
                    num /= 2;
                }
                Polynom m = new Polynom(coeff, k - 1); // �������� ���������
                Polynom m_p = m.mult(new Polynom(deg, 1));//������ ����������� �����
                list_a.add(m_p.sum(m_p.mod(g)));//����������� � ������ ���������
            }
            Map<Double, Double> prob = new TreeMap<>();
            for (int p = 0; p <= 100; p += 10) { // ���� �� ������������ ������
                int count = 0;
                int error = 0;
                while (count != N) {
                    Polynom a = list_a.get((int) (Math.random() * list_a.size())); //��������� ������� �� ������ ��������� ��������� ���������� ������������ ������������������
                    coeff = new LinkedList<>();
                    for (int i = 0; i <= k - 1 + deg; i++) //����������� ������ ������ � ����������� � ������� ������ �� ������� 7 �� ���������
                        if (a.getCoeff(i) != 0) { // ���� ����������� ������� �� ��������� ������ � ����������� � ������������ ������
                            if ((Math.random()) <= (double) p / 100)
                                coeff.add(1);
                            else
                                coeff.add(0);
                        } else { // ���� ����������� 0, �� ����������� ��� ������
                            coeff.add(0);
                        }
                    Polynom e = new Polynom(coeff, k - 1 + deg);// �������� ������� �� ������� ������
                    Polynom b = a.xor(e);//�������������� �������� ���������
                    if (b.mod(g).isnull() && !e.isnull()) // ���� ������� ����� 0 � ������� ������ �� �������
                        error++; //������������� ������� ������
                    count++; // ������������� ���������� ���������
                }
                prob.put((double) p / 100, (double) error / count); // ��������� ������ ����������� ������ �� ��� -- ����������� ����� ��� ����� ���������
            }
            System.out.println(prob);
            try (PrintWriter writer = new PrintWriter(new File("result"+deg+".csv"))) { // ������ � ���� ��� ������ �� �������
                for (Map.Entry<Double, Double> entry : prob.entrySet()) {
                    writer.write(entry.getKey() + "," + entry.getValue() + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
