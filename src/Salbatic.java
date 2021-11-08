import java.util.Arrays;

public class Salbatic extends Thread {
    Oala oala;
    int id;
    static Pregatar scarlatescu;
    static int[] nr_mancat;
    public Salbatic(Oala oala,Pregatar scarlatescu, int id,int[] nr_mancat) {
        this.oala = oala;
        this.id=id;
        Salbatic.scarlatescu= scarlatescu;
        Salbatic.nr_mancat = nr_mancat;

    }

    @Override
    public void run(){
        int chemat_bucatar=0;
        while(true){
            if(chemat_bucatar==1){
                chemat_bucatar=0;
                oala.lock_without_label(id);
            }else{
                oala.lock(id);// Blocheaza accesul asupra oalei
            }
            if(oala.getCurent()==0){ // Oala este goala
                oala.unlock(id); // Deblocheaza accesul asupra oalei
                //System.out.println("Salbaticul "+id+" da drumu la oala si il cheama pe scarlatescu sa umple oala!");
                Salbatic.scarlatescu.lock(id); // Blocheaza accesul asupra bucatarului
                Salbatic.scarlatescu.bagaMancare(id,oala.label[id]);
                Salbatic.scarlatescu.unlock(id); // Deblocheaza accesul asupra bucatarului
                //oala.lock(id);
                chemat_bucatar=1;
                continue;
            }
            oala.mananca(id);
            nr_mancat[id-1]++;
            oala.unlock(id); // Deblocheaza accesul asupra oalei
            //break; // Trebuie decomentat pt subpunctul A
        }
    }
}
