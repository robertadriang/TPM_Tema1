import java.io.PrintWriter;
import java.util.Arrays;

public class Oala {
    int maxim;
    int curent;
    int nr_salbatici;
    int[] label;
    boolean[] flag;
    static int[] nr_mancat;
    int[] chemat;
    static public long startTime = 0;
    PrintWriter out;
    public Oala(int maxim, int nr_salbatici, int[] nr_mancat, PrintWriter out) {
        System.out.println("S-a initializat oala!");
        this.maxim = maxim;
        this.curent = maxim;
        this.nr_salbatici = nr_salbatici + 1;
        this.label = new int[this.nr_salbatici];
        this.flag = new boolean[this.nr_salbatici];
        Oala.nr_mancat = nr_mancat;
        this.chemat = new int[this.nr_salbatici];
        this.out = out;
    }

    void mananca(int id) {
        curent--;
        for(int i = 0; i < nr_salbatici - 1; ++i){
            if(nr_mancat[i] == 0)
                return;
        }

        long duration = (System.nanoTime() - startTime)/1000000;  //divide by 1000000 to get milliseconds.
        out.println(duration);
        out.flush();
        System.exit(1);
    }

    void reumple() {
        curent = maxim;
        System.out.println("S-a umplut oala!");
        System.out.println("Label " + Arrays.toString(label));
        System.out.println(Arrays.toString(nr_mancat));
        System.out.println(Arrays.toString(this.chemat));

    }

    boolean isFirstBigger(int i, int j) {
        /// (a,b)>(c,d) IFF a>c OR a=c and b>d
        return label[i] > label[j] || (label[i] == label[j] && i > j);
    }

    boolean bakeryCondition(int i) {
        //exists k!=i with flag[k]==true && (label[i],i) > (label[k],k)
        for (int k = 0; k < this.nr_salbatici; ++k) {
            if (k != i && flag[k] && isFirstBigger(i, k))
                return true;
        }
        return false;
    }

    void lock(int id) {
        if (id == 0) {
            flag[id] = true;
            label[id] = -1;
            return;
        }
        //System.out.println("Salbaticul "+id+" a blocat oala");
        flag[id] = true;
        int maxim = 0;
        for (int j = 0; j < this.nr_salbatici; ++j) {
            if (maxim < label[j]) {
                maxim = label[j];
            }
        }
        label[id] = maxim + 1;
        //while (exists k!=i with flag[k]==true && (label[i],i) > (label[k],k)) {};
        while (bakeryCondition(id)) {
        }
    }

    void unlock(int id) {
        flag[id] = false;
    }

    public int getCurent() {
        return curent;
    }
}
