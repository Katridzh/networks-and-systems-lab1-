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
            LinkedList<Integer> coeff = new LinkedList<>();//задаются коэффициенты порождающего многочлена
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
            Polynom g = new Polynom(coeff, deg);//создаётся многочлен
            int k = 4;
            double eps = 0.005;
            int N = (int) (9 / (4 * pow(eps, 2))); // подсчитывается количество испытаний
            LinkedList<Polynom> list_a = new LinkedList<>(); // список возможных передаваемых сообщений
            for (int i = 0; i < pow(2, k - 1); i++) { // формируются все возможные сообщения
                int num = i;
                coeff = new LinkedList<>();
                coeff.add(1);
                for (int j = 1; j < k; j++) {
                    coeff.add(num % 2);
                    num /= 2;
                }
                Polynom m = new Polynom(coeff, k - 1); // создаётся многочлен
                Polynom m_p = m.mult(new Polynom(deg, 1));//расчёт контрольной суммы
                list_a.add(m_p.sum(m_p.mod(g)));//добавляется в список сообщений
            }
            Map<Double, Double> prob = new TreeMap<>();
            for (int p = 0; p <= 100; p += 10) { // цикл по вероятностям ошибки
                int count = 0;
                int error = 0;
                while (count != N) {
                    Polynom a = list_a.get((int) (Math.random() * list_a.size())); //случайным образом из списка возможных сообщений выбирается передаваемая последовательность
                    coeff = new LinkedList<>();
                    for (int i = 0; i <= k - 1 + deg; i++) //формируется вектор ошибок в соответсвии с моделью канала на рисунке 7 из методички
                        if (a.getCoeff(i) != 0) { // если принималась единица то формируем ошибку в соответсвии с вероятностью ошибки
                            if ((Math.random()) <= (double) p / 100)
                                coeff.add(1);
                            else
                                coeff.add(0);
                        } else { // если передавался 0, то принимается без ошибки
                            coeff.add(0);
                        }
                    Polynom e = new Polynom(coeff, k - 1 + deg);// создаётся полином из вектора ошибок
                    Polynom b = a.xor(e);//рассчитывается принятое сообщение
                    if (b.mod(g).isnull() && !e.isnull()) // если синдром равен 0 и векстор ошибок не нулевой
                        error++; //увеличивается счётчик ошибок
                    count++; // увеличивается количество испытаний
                }
                prob.put((double) p / 100, (double) error / count); // формируем список вероятность ошибки на бит -- вероятность оибки при приёме сообщения
            }
            System.out.println(prob);
            try (PrintWriter writer = new PrintWriter(new File("result"+deg+".csv"))) { // запись в файл для вывода на графике
                for (Map.Entry<Double, Double> entry : prob.entrySet()) {
                    writer.write(entry.getKey() + "," + entry.getValue() + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
