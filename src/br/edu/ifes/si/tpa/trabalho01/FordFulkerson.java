package br.edu.ifes.si.tpa.trabalho01;

import java.util.Queue;


public class FordFulkerson {
    private static final double FLOATING_POINT_EPSILON = 1E-11;
    private final int V;
    private boolean[] marcado;
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
        bordaPara = new RedeFluxo[G.V()];
        marcado= new boolean[G.V()];

        // breadth-first search
        Queue<Integer> queue = new Queue<Integer>();
        queue.enqueue(s);
        marcado[s] = true;
        while (!queue.isEmpty() && !marcado[t]) {
            int v = queue.dequeue();

            for (ArestaFluxo e : G.adj(v)) {
                int w = e.other(v);

                // if residual capacity from v to w
                if (e.residualCapacityTo(w) > 0) {
                    if (!marcado[w]) {
                        bordaPara[w] = e;
                        marcado[w] = true;
                        queue.enqueue(w);
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

    private boolean isFeasible(RedeFluxo G, int s, int t) {

        // check that capacity constraints are satisfied
        for (int v = 0; v < G.V(); v++) {
            for (RedeFluxo e : G.adj(v)) {
                if (e.flow() < -FLOATING_POINT_EPSILON || e.flow() > e.capacity() + FLOATING_POINT_EPSILON) {
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
}