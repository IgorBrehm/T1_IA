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
        this.best_path = new String[10];
        this.best_path = initialize_path(best_path);
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
    
    private String[] initialize_path(String[] path){
        Random random = new Random();
        int j = 0;
        for(int i = 0; i < path.length; i++){
            j = random.nextInt(4);
            if(j == 1){ // 1
                path[i] = "L";
            }
            else if(j == 2){ // 2
                path[i] = "R";
            }
            else if(j == 3){ // 3
                path[i] = "U";
            }
            else{ // 4
                path[i] = "D";
            }
        }
        return path;
    }
    
    // Este metodo calcula o quao boa e a sequencia de direcoes sendo avaliada
    /*
     * Score: 
     * 1. Quantos passos da sem bater em nada: Cada passo aumenta score em 5% do total de posicoes do labirinto
     * 2. Penalizar movimentos em 'circulo': Cada comportamento circular detectado penaliza em 10% do total de posicoes do labirinto
     * 3. Chega na saida? Ganha muito score: automaticamente recebe 80% do total de posicoes no score (sem levar em conta bonus e penalidades).
     */
    private int evaluated_Distribution(String[] path){
        int score = 0;
        int percent = Math.round((1/dimension)*100);
        int current_x = 0;
        int current_y = 0;
        for(int k = 0; k < path.length; k++){
            int[] feedback = new int[3];
            feedback = move(path[k], current_x, current_y);
            current_x = feedback[1];
            current_y = feedback[2];
            if(feedback[0] == 0){
                break; //bateu
            }
            else if(feedback[0] == 1){ // 1
                score = score + (percent * 5);
            }
            else if (feedback[0] == 2){ // 2
                score = score - (percent * 10);
            }
            else{ // 3
                score = score + (percent * 80);
                break; //chegou na saida
            }
        }
        System.out.println("Score: "+ score);
        return score;
    }
    
    private int[] move(String movement, int position_x, int position_y){
        int[] exit_status = new int[3];
        int x;
        int y;
        if(movement.equals("L")){ // Left
            // checar se sai fora do labirinto com o movimento
            // se nao sair, checar se a posicao tem o que escrito
            if(labyrinth[position_x][position_y].equals("0") || labyrinth[position_x][position_y].equals("E")){ // 1
                exit_status = {1,x,y};
                return exit_status;    
            }
            else if(){ // 2
                exit_status = {2,x,y};
                return exit_status;
            }
            else if(){ // 3
                exit_status = {3,x,y};
                return exit_status;
            }
            else{
                exit_status = {0,x,y};
                return exit_status;
            }
        }
        else if(movement.equals("R")){ // Right
            if(labyrinth[position_x][position_y].equals("0") || labyrinth[position_x][position_y].equals("E")){ // 1
                exit_status = {1,x,y};
                return exit_status;    
            }
            else if(){ // 2
                exit_status = {2,x,y};
                return exit_status;
            }
            else if(){ // 3
                exit_status = {3,x,y};
                return exit_status;
            }
            else{
                exit_status = {0,x,y};
                return exit_status;
            }
        }
        else if(movement.equals("U")){ // Up
            if(labyrinth[position_x][position_y].equals("0") || labyrinth[position_x][position_y].equals("E")){ // 1
                exit_status = {1,x,y};
                return exit_status;    
            }
            else if(){ // 2
                exit_status = {2,x,y};
                return exit_status;
            }
            else if(){ // 3
                exit_status = {3,x,y};
                return exit_status;
            }
            else{
                exit_status = {0,x,y};
                return exit_status;
            }
        }
        else(D){ // Down
            if(labyrinth[position_x][position_y].equals("0") || labyrinth[position_x][position_y].equals("E")){ // 1
                exit_status = {1,x,y};
                return exit_status;    
            }
            else if(){ // 2
                exit_status = {2,x,y};
                return exit_status;
            }
            else if(){ // 3
                exit_status = {3,x,y};
                return exit_status;
            }
            else{
                exit_status = {0,x,y};
                return exit_status;
            }
        }
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
        double T = 300;
        for(int t = 1; t < 10000; t++){  
            find_Successor(T);
            if(evaluated_Distribution(work_hours) >= (dimension * dimension)){
                break;
            }    
            T = T*0.99;
        }
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
