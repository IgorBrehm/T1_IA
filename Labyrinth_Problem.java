/**
 * Uma solução para o problema do labirinto usando o algoritmo
 * de tempera simulada. 
 * 
 * @author Igor Sgorla Brehm, Alexandre Bing e Vinicius Parmeggiani
 */
import java.util.Random;
import java.util.Scanner;
import java.io.*;

public class Labyrinth_Problem {
    private String[][] labyrinth;
    
    // Inicializador do objeto
    public Labyrinth_Problem() throws FileNotFoundException{
        File file = new File(System.getProperty("user.dir")+"/lab_exemplo1.txt");
        Scanner in = new Scanner(file);
        int dimension = in.nextInt();
        this.labyrinth = new String[dimension][dimension];
        System.out.print(" ");
        for(int i = 0; i < dimension-1; i++){
            System.out.print("___");
        }
        System.out.print("_");
        System.out.println("");
        for(int i = 0; i < dimension; i++){
            System.out.print("|");
            for(int j = 0; j < dimension; j++){
                labyrinth[i][j] = in.next();
                if(j == dimension - 1){
                    System.out.print(" "+labyrinth[i][j]);
                }
                else if(j == 0){
                    System.out.print(labyrinth[i][j]+" ");
                }
                else{
                    System.out.print(" "+labyrinth[i][j]+" ");    
                }
            }
            System.out.print("|");
            System.out.println("");
        }
        simulated_Annealing();
    }
    
    // Este metodo compara a diferenca de horas a serem trabalhadas entre as duas pessoas
    public int evaluated_Distribution(int[] vector){
        return 0;
        /*
        int p1 = 0; 
        int p2 = 0;
        for(int i = 0; i < total_tasks/2; i++) {
            p1 = p1 + vector[i];
        }
        for(int i = total_tasks/2; i < total_tasks; i++) {
            p2 = p2 + vector[i];
        }
        if(p1 < p2){
            return p2 - p1;
        }
        else if(p1 > p2){
            return p1 - p2;
        }
        else{
            return p1 - p2;
        }    
        */
    }
    
    // Este metodo tenta encontrar uma distribuicao melhor de horas entre as duas pessoas
    public void find_Successor(double T){
        return;
        /*
        Random index = new Random();
        int j = 0;
        int i = 0;
        while(i == j){
            j = index.nextInt(total_tasks);
            i = index.nextInt(total_tasks);    
        }
        int[] candidate = work_hours;
        int aux = candidate[j];
        candidate[j] = candidate[i];
        candidate[i] = aux;
        int actual = evaluated_Distribution(work_hours);
        int next = evaluated_Distribution(candidate);
        double random_dropout = Math.exp(-(next-actual)/T);
        if(actual > next){
            work_hours = candidate;
            return;
        }
        else if(random_dropout > 0) {
            work_hours = candidate;
            return;
        }
        else{
            return;
        }
        */
    }
    
    // Metodo que simula o algoritmo de tempera simulada
    public void simulated_Annealing(){
        return;
        /*
        double T = 300;
        for(int t = 1; t < 10000; t++){  
            find_Successor(T);
            if(evaluated_Distribution(work_hours) == 0){
                break;
            }    
            T = T*0.99;
        }
        */
    }
    
    
    // Metodo Main da solucao
    public static void main(String args[]) throws FileNotFoundException{
       Labyrinth_Problem problem = new Labyrinth_Problem();
    }
}
