import java.util.Set;
import java.util.Stack;

import pda.automata.IPDA;
import pda.automata.IState;
import pda.automata.ITransition;

// Esta é a base de código que implementa um PDA, especialmente o método RUN
public class PDAImpl implements IPDA {
    protected IState q; // Estado inicial do PDA
    protected final Stack<Character> pilha = new Stack<>(); // Pilha de acesso
    protected boolean log = false; // Se tem ou não log

    public PDAImpl(IState q) {
        this.q = q;
        pilha.add('#'); // Empilha o símbolo inicial
    }

    @Override
    public boolean run(String w) {
        char[] texto_fonte = w.toCharArray();
        IState estadoAtual = q;

        for (char character : texto_fonte) {
            Set<ITransition> transitions = estadoAtual.transitions(character, pilha.peek());

            boolean transitionFound = false;
            for (ITransition transition : transitions) {
                pilha.pop();
                pilha.push(transition.getEdge().getPush());

                estadoAtual = transition.getState();
                transitionFound = true;
                break;
            }

            if (!transitionFound) {
                return false;
            }
        }

        return estadoAtual.isFinal() && pilha.peek() == '#';
    }

	@Override
	public void makeLog() {
		//TODO: insira seu codigo aqui caso deseje fazer log. Sugestao log = true;
	}
}
