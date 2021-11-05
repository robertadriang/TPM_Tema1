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
        this.nr_mancat=nr_mancat;

    }

    @Override
    public void run(){
        while(true){
            oala.lock(id);
            if(oala.curent==0){
                oala.unlock(id);
                //System.out.println("Salbaticul "+id+" da drumu la oala si il cheama pe scarlatescu sa umple oala!");
                Salbatic.scarlatescu.lock(id);
                Salbatic.scarlatescu.bagaMancareLaFlamanzi(id);
                Salbatic.scarlatescu.unlock(id);
                //oala.lock(id);
                // Wer mancare
                continue;
            }
            oala.mananca(id);
            nr_mancat[id-1]++;
            oala.unlock(id);
        }
    }
}
