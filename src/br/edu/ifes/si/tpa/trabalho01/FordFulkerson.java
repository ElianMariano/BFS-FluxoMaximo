package br.edu.ifes.si.tpa.trabalho01;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;


public class FordFulkerson {
    private static final String NEWLINE = System.getProperty("line.separator");
    
    private static final double FLOATING_POINT_EPSILON = 1E-11;
    private final int V;
    private boolean[] marcado;
    private ArestaFluxo[] bordaPara;
    private double valor;
    
    public FordFulkerson(RedeFluxo G, int s, int t){
        V = G.V();
        validar(s);
        validar(t);
        if (s == t)            throw new IllegalArgumentException("Fonte é igual a sumidouro");
        if (!eViavel(G, s, t)) throw new IllegalArgumentException("Fluxo inicial é inviável");

        valor = excesso(G, t);
        while (temAumentoNoCaminho(G, s, t)) {

            double garrafa = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = bordaPara[v].outro(v)) {
                garrafa = Math.min(garrafa, bordaPara[v].capacidadeResidualPara(v));
            }

            for (int v = t; v != s; v = bordaPara[v].outro(v)) {
                bordaPara[v].addFluxoResidualPara(v, garrafa); 
            }

            valor += garrafa;
        }
        
        assert check(G, s, t);
    }
    
    public double valor(){
        return valor;
    }
    
    public boolean emCorte(int v){
        validar(v);
        return marcado[v];
    }
    
    private void validar(int v){
        if (v < 0 || v >= V)
            throw new IllegalArgumentException(String.format("Vertice %d não está entre 0 e %d", v, (V-1)));
    }

    private boolean temAumentoNoCaminho(RedeFluxo G, int s, int t) {
        bordaPara = new ArestaFluxo[G.V()];
        marcado = new boolean[G.V()];

        // breadth-first search
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        marcado[s] = true;
        while (!queue.isEmpty() && !marcado[t]) {
            int v = queue.remove();

            for (ArestaFluxo e : G.adj(v)) {
                int w = e.outro(v);

                // if residual capacity from v to w
                if (e.capacidadeResidualPara(w) > 0) {
                    if (!marcado[w]) {
                        bordaPara[w] = e;
                        marcado[w] = true;
                        queue.remove(w);
                    }
                }
            }
        }

        // is there an augmenting path?
        return marcado[t];
    }
    
    private double excesso(RedeFluxo G, int v){
        double excesso = 0.0;
        for (ArestaFluxo e : G.adj(v)){
            if (v == e.de()) excesso -= e.fluxo();
            else             excesso += e.fluxo();
        }
        return excesso;
    }

    private boolean eViavel(RedeFluxo G, int s, int t) {

        // check that capacity constraints are satisfied
        for (int v = 0; v < G.V(); v++) {
            for (ArestaFluxo e : G.adj(v)) {
                if (e.fluxo() < -FLOATING_POINT_EPSILON || e.fluxo() > e.capacidade() + FLOATING_POINT_EPSILON) {
                    System.err.println("A borda não atende às restrições de capacidade: " + e);
                    return false;
                }
            }
        }

        
        // check that net flow into a vertex equals zero, except at source and sink
        if (Math.abs(valor + excesso(G, s)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excess at source = " + excesso(G, s));
            System.err.println("Max flow         = " + valor);
            return false;
        }
        if (Math.abs(valor - excesso(G, t)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excesso    = " + excesso(G, t));
            System.err.println("Fluxo Máximo         = " + valor);
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s || v == t) continue;
            else if (Math.abs(excesso(G, v)) > FLOATING_POINT_EPSILON) {
                System.err.println("Fluxo líquido de" + v + " não é igual a zero");
                return false;
            }
        }
        return true;
    }
    
     private boolean check(RedeFluxo G, int s, int t) {
        if (!eViavel(G, s, t)) {
            System.err.println("Fluxo é inviavel");
            return false;
        }

        if (!emCorte(s)) {
            System.err.println("a fonte " + s + " não está do lado da fonte do corte mínimo");
            return false;
        }

        if (!emCorte(t)) {
            System.err.println("o coletor " + t + " está no lado da fonte do corte mínimo");
            return false;
        }

        double ValorMinimoDeCorte = 0.0;
        for (int v = 0; v < G.V(); v++) {
            for (ArestaFluxo e : G.adj(v)) {
                if ((v == e.de()) && emCorte(e.de()) && !emCorte(e.para()))
                    ValorMinimoDeCorte += e.capacidade();
            }
        }

        if (Math.abs(ValorMinimoDeCorte - valor) > FLOATING_POINT_EPSILON) {
            System.err.println("Valor de Fluxo Maximo = " + valor + ", Valor de Corte Minimo = " + ValorMinimoDeCorte);
            return false;
        }

        return true;
    }
     
    public static void main(String[] args) {
//        StringBuilder builder = new StringBuilder();
         
        // create flow network with V vertices and E edges
        In in = new In(args[0]);
        RedeFluxo G = new RedeFluxo(in);
        System.out.println(G);

        int s = 0;
        int t = G.V() - 1;

        // compute maximum flow and minimum cut
        FordFulkerson maxflow = new FordFulkerson(G, s, t);
        System.out.println("Fluxo máximo de " + s + " para " + t);
//        builder.append("Fluxo máximo de " + s + " para " + t + NEWLINE);
        for (int v = 0; v < G.V(); v++) {
            for (ArestaFluxo e : G.adj(v)) {
                if ((v == e.de()) && e.fluxo() > 0)
//                    builder.append("   " + e + NEWLINE);
                    System.out.println("   " + e);
            }
        }

        // print min-cut
//        StdOut.print("Min cut: ");

        for (int v = 0; v < G.V(); v++) {
//            if (maxflow.emCorte(v)) builder.append(v + " ");
            if (maxflow.emCorte(v)) System.out.println(v + " ");;
        }
//        StdOut.println();
        System.out.println("");

//        builder.append("Valor do fluxo máximo = " +  maxflow.valor());
        System.out.println("Valor do fluxo máximo = " +  maxflow.valor());
    }
}