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
    private String[][] backup_labyrinth;
    private int dimension;
    private String[] best_path;
    private int best_score;
    private int trouble_index;
    
    // Inicializador do objeto
    public Labyrinth_Problem() throws FileNotFoundException{
        File file = new File(System.getProperty("user.dir")+"/lab_exemplo2.txt");
        Scanner in = new Scanner(file);
        this.dimension = in.nextInt();
        this.trouble_index = 0;
        this.best_path = new String[dimension*dimension];
        this.best_path = initialize_path(best_path,"start");
        this.labyrinth = new String[dimension][dimension];
        this.backup_labyrinth = new String[dimension][dimension];
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                labyrinth[i][j] = in.next();
            }
        }
        in.close();
        this.best_score = evaluate_Path(best_path,"Best Path");
        System.out.println("Iniciada a execucao do algoritmo...\n");
        System.out.println("Labirinto a ser resolvido:");
        print_Labyrinth();
        simulated_Annealing();
    }
    
    // Metodo que inicializa os valores do vetor de direcoes
    private String[] initialize_path(String[] path, String command){
        Random random = new Random();
        int j = 0;
        if(command.equals("improve")){ // candidate
            for(int i = 0; i < trouble_index; i++){
                path[i] = best_path[i];
            }
            for(int i = trouble_index; i < path.length; i++){
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
        }
        else if(command.equals("cut")){
            int aux = (int) Math.floor(trouble_index/2);
            for(int i = 0; i < aux; i++){
                path[i] = best_path[i];
            }
            for(int i = aux; i < path.length; i++){
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
        }
        else{
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
        }
        return path;
    }
    
    // Este metodo calcula o quao boa e a sequencia de direcoes sendo avaliada
    /*
     * Score: 
     * 1. Quantos passos da sem bater em nada: Cada passo aumenta score em 1% do total de posicoes do labirinto
     * 2. Penalizar movimentos em 'circulo': Cada comportamento circular detectado penaliza em 10% do total de posicoes do labirinto
     * 3. Chega na saida? Ganha muito score: automaticamente recebe 80% do total de posicoes no score (sem levar em conta bonus e penalidades).
     */
    private int evaluate_Path(String[] path,String name){
        int score = 0;
        int current_x = 0;
        int current_y = 0;
        print_Path(path);
        System.out.println("Avaliando "+name);
        for(int k = 0; k < path.length; k++){
            int[] feedback = new int[3];
            System.out.println("Movendo para: "+path[k]);
            System.out.println("Posicao atual: "+labyrinth[current_x][current_y]+"|"+current_x+"|"+current_y);
            feedback = move(path[k], current_x, current_y);
            current_x = feedback[1];
            current_y = feedback[2];
            if(feedback[0] == 0){
                System.out.println("BATEU!");
                if(name.equals("Best Path")){
                    trouble_index = k;
                }
                break; // 0 bateu na parede
            }
            else if(feedback[0] == 1){
                if(detect_circle(path,k,current_x,current_y) == true){ // 2 movimento em circulo
                    score = score - 3;
                    score = score + 1;
                    System.out.println("ANDOU EM CIRCULO!");
                }
                else{ // 1 movimento bom
                    score = score + 1; 
                    System.out.println("ANDOU COM SUCESSO!");
                }
            }
            else{ // 3 chegou na saida
                System.out.println("CHEGOU NA SAIDA!");
                score = score + (dimension*dimension);
                // *********************************************************************
                // TODO: agora que chegamos na saida, o score deve funcionar diferente
                // ja que agora queremos encontrar o menor caminho ate a saida
                // *********************************************************************
                break;
            }
        }
        return score;
    }
    
    // Metodo que realiza o movimento atual dentro do labirinto
    private int[] move(String movement, int position_x, int position_y){
        int[] exit_status;
        int x = position_x;
        int y = position_y;
        if(movement.equals("U")){
            boolean valid = check_move_validity("U",position_x,position_y);
            if(valid == false){ // 0 bateu
                exit_status = new int[]{0,x,y};
                return exit_status;
            }
            else{
                if(labyrinth[position_x-1][position_y].equals("0")|| labyrinth[position_x-1][position_y].equals("E")){ // 1 valido
                    labyrinth[position_x][position_y] = "$";
                    x = position_x - 1;
                    y = position_y;
                    exit_status = new int[]{1,x,y};
                    return exit_status;    
                }
                else if(labyrinth[position_x-1][position_y].equals("S")){ // 3 chegou na saida!
                    labyrinth[position_x][position_y] = "$";
                    x = position_x - 1;
                    y = position_y;
                    exit_status = new int[]{3,x,y};
                    return exit_status;
                }
                else{ // 0 bateu
                    exit_status = new int[]{0,x,y};
                    return exit_status;
                }
            }
        }
        else if(movement.equals("D")){
            boolean valid = check_move_validity("D",position_x,position_y);
            if(valid == false){ // 0 bateu
                exit_status = new int[]{0,x,y};
                return exit_status;
            }
            else{
                if(labyrinth[position_x+1][position_y].equals("0")|| labyrinth[position_x+1][position_y].equals("E")){ // 1 valido
                    labyrinth[position_x][position_y] = "$";
                    x = position_x + 1;
                    y = position_y;
                    exit_status = new int[]{1,x,y};
                    return exit_status;    
                }
                else if(labyrinth[position_x+1][position_y].equals("S")){ // 3 chegou na saida!
                    labyrinth[position_x][position_y] = "$";
                    x = position_x + 1;
                    y = position_y;
                    exit_status = new int[]{3,x,y};
                    return exit_status;
                }
                else{ // 0 bateu
                    exit_status = new int[]{0,x,y};
                    return exit_status;
                }
            }
        }
        else if(movement.equals("L")){
            boolean valid = check_move_validity("L",position_x,position_y);
            if(valid == false){ // 0 bateu
                exit_status = new int[]{0,x,y};
                return exit_status;
            }
            else{
                if(labyrinth[position_x][position_y-1].equals("0")|| labyrinth[position_x][position_y-1].equals("E")){ // 1 valido
                    labyrinth[position_x][position_y] = "$";
                    x = position_x;
                    y = position_y-1;
                    exit_status = new int[]{1,x,y};
                    return exit_status;    
                }
                else if(labyrinth[position_x][position_y-1].equals("S")){ // 3 chegou na saida!
                    labyrinth[position_x][position_y] = "$";
                    x = position_x;
                    y = position_y-1;
                    exit_status = new int[]{3,x,y};
                    return exit_status;
                }
                else{ // 0 bateu
                    exit_status = new int[]{0,x,y};
                    return exit_status;
                }
            }
        }
        else{
            boolean valid = check_move_validity("R",position_x,position_y);
            if(valid == false){ // 0 bateu
                exit_status = new int[]{0,x,y};
                return exit_status;
            }
            else{
                if(labyrinth[position_x][position_y+1].equals("0")|| labyrinth[position_x][position_y+1].equals("E")){ // 1 valido
                    labyrinth[position_x][position_y] = "$";
                    x = position_x;
                    y = position_y+1;
                    exit_status = new int[]{1,x,y};
                    return exit_status;    
                }
                else if(labyrinth[position_x][position_y+1].equals("S")){ // 3 chegou na saida!
                    labyrinth[position_x][position_y] = "$";
                    x = position_x;
                    y = position_y+1;
                    exit_status = new int[]{3,x,y};
                    return exit_status;
                }
                else{ // 0 bateu
                    exit_status = new int[]{0,x,y};
                    return exit_status;
                }
            }
        }
    }
    
    
    // Metodo que verifica a validade do movimento, isto e, se nao vai sair fora do labirinto
    private boolean check_move_validity(String move,int pos_x,int pos_y){
        if(move.equals("U")){ //Up
            if(pos_x == 0){
                return false;
            }
            else{
                return true;
            }
        }
        else if(move.equals("D")){ //Down
            if(pos_x == dimension-1){
                return false;
            }
            else{
                return true;
            }
        }
        else if(move.equals("L")){ //Left
            if(pos_y == 0){
                return false;
            }
            else{
                return true;
            }
        }
        else{ //Right
            if(pos_y == dimension-1){
                return false;
            }
            else{
                return true;
            }
        }
    }
    
    // Este metodo tenta identificar movimentos em circulo
    private boolean detect_circle(String[] path, int moves, int x, int y){
        if(moves == 0){
            return false;
        }
        else{
            if(path[moves-1].equals("L") && path[moves].equals("R")){
                return true;   
            }
            else if(path[moves-1].equals("R") && path[moves].equals("L")){
                return true;
            }
            else if(path[moves-1].equals("D") && path[moves].equals("U")){
                return true;
            }
            else if(path[moves-1].equals("U") && path[moves].equals("D")){
                return true;    
            }
            else if(labyrinth[x][y].equals("$")){
                return true;
            }
            else{
                return false;
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
    public void find_Successor(int T) throws FileNotFoundException{
        Random index = new Random();
        String aux = "";
        String[] candidate = new String[dimension*dimension];
        candidate = initialize_path(candidate,"improve");
        int next = evaluate_Path(candidate,"Candidate");
        print_Labyrinth();
        reset_Labyrinth();
        int random_dropout = index.nextInt(5000)-T;
        if(best_score < next){
            best_path = candidate;
            best_score = evaluate_Path(best_path,"Best Path");
            return;
        }
        else if(random_dropout <= 0) {
            System.out.println("RANDOM DROPOUT! ");
            best_path = candidate;
            best_score = evaluate_Path(best_path,"Best Path");
            best_path = initialize_path(best_path,"cut");
            return;
        }
        else{
            return;
        }
    }
    
    // Metodo que simula o algoritmo de tempera simulada
    public void simulated_Annealing() throws FileNotFoundException{
        int T = 5000;
        for(int t = 1; t < 10000; t++){
            find_Successor(T);
            if(best_score >= (dimension * dimension)){
                System.out.println("Solucao satisfatoria encontrada: "+best_score);
                print_Path(best_path);
                print_Labyrinth();
                break;
            }    
            else{
                System.out.println("SCORE ATUAL: "+best_score);
            }
            T = T-1;
        }
    }
    
    // Reseta o labirinto
    private void reset_Labyrinth() throws FileNotFoundException{
        File file = new File(System.getProperty("user.dir")+"/lab_exemplo2.txt");
        Scanner in = new Scanner(file);
        dimension = in.nextInt();
        labyrinth = new String[dimension][dimension];
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                labyrinth[i][j] = in.next();
            }
        }
        in.close();
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
