package br.edu.ifes.si.tpa.trabalho01;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;


public class FordFulkerson {
    private static final String NEWLINE = System.getProperty("line.separator");
    
    private static final double FLOATING_POINT_EPSILON = 1E-11;
    // Quantidade de vertices
    private final int V;
    // Vertices que estão marcados na Rede de Fluxo
    private boolean[] marcado;
    // 
    private ArestaFluxo[] bordaPara;
    // Valor final da rede de fluxo
    private double valor;
    
    public FordFulkerson(RedeFluxo G, int s, int t){
        // Obtêm a quantidade de vertices
        V = G.V();
        // Inicia o valor como 0
        valor = 0;
        // Inicia o array da variavel marcado
        marcado = new boolean[G.V()];

        // Calcular o FordFulkerson
        temAumentoNoCaminho(G, s, t);
    }
    
    // Retorna o valor final do algotitmo FordFulkerson
    public double valor(){
        return valor;
    }
    
    // Procura um almento no caminho
    private boolean temAumentoNoCaminho(RedeFluxo G, int s, int t) {
        bordaPara = new ArestaFluxo[G.V()];
        
        // Marca se ja verificou o vertice
        boolean[] verificado = new boolean[G.V()];

        // breadth-first search
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        marcado[s] = true;
        System.out.println(String.format("W: %d", s));
        
        // Vertice atual
        int atual = s;
        while(!marcado[t]){
            for (ArestaFluxo e : G.adj(atual)){
                for (ArestaFluxo e2 : G.adj(atual)){
                    int w = e2.para(); //e.outro(atual);
                    if (!verificado[w] && !marcado[w]){ // Verificar a viabilidade 
                        marcado[w] = true;
                        System.out.println(String.format("W: %d", w));
                    }
                }
                verificado[atual] = true;
                atual++;
            }
        }
        return true;
    }
    
    // Mostra o caminho de aumento no console
    private void mostraCaminhoDeAumento(Queue caminho){
        int i = 0;
        System.out.print(String.format("%d: ", i));
        while (!caminho.isEmpty()){
            System.out.print(String.format("%d", caminho.remove()));
            
            if (caminho.size() >= 1) System.out.print("->");
        }
        System.out.print("\n");
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        RedeFluxo G = new RedeFluxo(in);
        System.out.println(G);
        
        FordFulkerson maxflow = new FordFulkerson(G, 0, 7);
    }
}