package br.edu.ifes.si.tpa.trabalho01;

import java.util.ArrayList;
import java.util.List;

public class RedeFluxo {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int A;
    private List<ArestaFluxo>[] adj;
    
    public RedeFluxo(int V) {
        if (V < 0) throw new IllegalArgumentException("Número de vértices do grafo deve ser não negativo");
        this.V = V;
        this.A = 0;
        adj = new ArrayList[V];
        for (int v = 0; v < V; v++)
            adj[v] = new ArrayList<>();
    }

    public RedeFluxo(In in) {
        this(in.readInt());
        int A = in.readInt();
        if (A < 0) throw new IllegalArgumentException("número de arestas no grafo deve ser não negativa");
        for (int i = 0; i < A; i++) {
            int v = in.readInt();
            int w = in.readInt();
            double capacity = in.readDouble();
            addAresta(new ArestaFluxo(v, w, capacity));
        }
    }

    public int V() {
        return V;
    }

    public int A() {
        return A;
    }

    public void addAresta(ArestaFluxo a) {
        int v = a.de();
        int w = a.para();
        adj[v].add(a);
        adj[w].add(a);
        A++;
    }

    public Iterable<ArestaFluxo> adj(int v) {
        return adj[v];
    }

    // return list of all arestas - excludes self loops
    public Iterable<ArestaFluxo> arestas() {
        List<ArestaFluxo> list = new ArrayList();
        for (int v = 0; v < V; v++)
            for (ArestaFluxo e : adj(v)) {
                if (e.para() != v)
                    list.add(e);
            }
        return list;
    }
    

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + A + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ":  ");
            for (ArestaFluxo e : adj[v]) {
                if (e.para() != v) s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        RedeFluxo G = new RedeFluxo(in);
        System.out.println(G);
    }

}