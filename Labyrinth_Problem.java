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
    private int dimension;
    private String[] best_path;
    
    // Inicializador do objeto
    public Labyrinth_Problem() throws FileNotFoundException{
        File file = new File(System.getProperty("user.dir")+"/lab_exemplo1.txt");
        Scanner in = new Scanner(file);
        best_path = new String[]{"N","N","N","N","N","N","N","N","N","N"};
        this.dimension = in.nextInt();
        this.labyrinth = new String[dimension][dimension];
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                labyrinth[i][j] = in.next();
            }
        }
        print_Labyrinth();
        simulated_Annealing();
    }
    
    // Este metodo calcula o quao boa e a sequencia de direcoes sendo avaliada
    /*
     * Score: 
     * 1. Quantos passos da sem bater em nada: Cada passo aumenta score em 5% do total de posicoes do labirinto
     * 2. Penalizar movimentos em 'circulo': Cada comportamento circular detectado penaliza em 10% do total de posicoes do labirinto
     * 3. Chega na saida? Ganha muito score: automaticamente recebe 80% do total de posicoes no score (sem levar em conta bonus e penalidades).
     */
    public int evaluated_Distribution(int[] path){
        int score = 0;
        int percent = Math.round((1/dimension)*100); 
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                /*
                if(){ // 1
                    score = score + (percent * 5);        
                }
                else if(){ // 2
                    
                }
                else if(){ // 3
                    
                }
                else{
                    continue;    
                }
                */
            }
        }
        System.out.println("Score: "+ score);
        return score;
    }
    
    // Este metodo tenta encontrar um estado vizinho (combinacao de direcoes com apenas 2 direcoes de diferenca do estado atual)
    // que seja melhor que o estado atual.
    /*
     * Direcoes possiveis:
     * 1. L -> Esquerda
     * 2. R -> Direita
     * 3. U -> Cima
     * 4. D -> Baixo
     * 
     * Tipos de posicoes no labirinto:
     * 1. E -> Entrada
     * 2. S -> Saida
     * 3. 0 -> Passagem
     * 4. 1 -> Parede
     */
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
    
    //Metodo para escrever na tela o labirinto atual
    public void print_Labyrinth(){
        System.out.print(" ");
        for(int i = 0; i < dimension-1; i++){
            System.out.print("___");
        }
        System.out.print("_");
        System.out.println("");
        for(int i = 0; i < dimension; i++){
            System.out.print("|");
            for(int j = 0; j < dimension; j++){
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
        System.out.println("");
    }
    
    //Metodo para escrever na tela uma sequencia de direcoes dentro do labirinto
    public void print_Path(String[] path){
        for(int i = 0; i < path.length-1; i++){
            System.out.print(path[i]+" -> ");    
        }
        System.out.print(path[path.length-1]);
        System.out.println("\n");
    }
    
    // Metodo Main da solucao
    public static void main(String args[]) throws FileNotFoundException{
       Labyrinth_Problem problem = new Labyrinth_Problem();
    }
}
