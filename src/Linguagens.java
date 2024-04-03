import java.util.Arrays;
import java.util.List;

import pda.automata.IPDA;
import pda.automata.IState;
import pda.automata.lib.PDA;

public class Linguagens {// PDA = (Q, Σ, δ, {qi}, F)	
	public static void expression() throws Exception {
		/*  E -> T+E
			E -> T
			T -> F*E
			T -> F
			T -> F==F
			T -> I
			F -> aF | a
			F -> (E)
			I -> if(T){E}
		 */
		
		char[] a_z = " abcdefghijklmnopqrstuvxywz".toCharArray();
		int n = 200; int j = 0;
		IState qi = new State("qi");
		IState qs = new State("qs");
		IState qloop = new State("qloop");
		IState qf = new State("qf");
		
		IState[] sts = new State[n];
		for(int i = 0; i < n; i++) {
			sts[i] = new State("q"+i);
		}
		qf.setFinal();
		//Base
		qi.addTransition(qs, null, null, '$');
		qs.addTransition(qloop, null, null, 'E');
		qloop.addTransition(qf, null, '$', null);
		
		//E -> T+E
		qloop.addTransition(sts[j], null, 'E', 'E');
		sts[j].addTransition(sts[j+1], null, null, '+');
		sts[++j].addTransition(qloop, null, null, 'T');
		qloop.addTransition(qloop, null, 'E', 'T');
		
		//T -> F*E
		qloop.addTransition(sts[++j], null, 'T', 'E');
		sts[j].addTransition(sts[j+1], null, null, '*');
		sts[++j].addTransition(qloop, null, null, 'F');
		
		//T -> F==F
		qloop.addTransition(sts[++j], null, 'T', 'F');
		sts[j].addTransition(sts[j+1], null, null, '='); ++j;
		sts[j].addTransition(sts[j+1], null, null, '='); ++j;
		sts[j].addTransition(qloop, null, null, 'F');		
		
		//T -> F
		qloop.addTransition(qloop, null, 'T', 'F');
		//T -> I
		qloop.addTransition(qloop, null, 'T', 'I');

		// F -> D
		qloop.addTransition(qloop, null, 'F', 'D');
		for (char c : a_z) { 
			qloop.addTransition(sts[++j], null, 'D', 'D');
			sts[j].addTransition(qloop, null, null, c);
			qloop.addTransition(qloop, null, 'D', c);
		}
		
		//F -> (E)
		qloop.addTransition(sts[++j], null, 'F', ')');
		sts[j].addTransition(sts[j+1], null, null, 'E');
		sts[++j].addTransition(qloop, null, null, '(');		
		
		// I -> if(F){E}
		qloop.addTransition(sts[++j], null, 'I', '}');
		sts[j].addTransition(sts[j+1], null, null, 'E'); j++;
		sts[j].addTransition(sts[j+1], null, null, '{'); j++;
		sts[j].addTransition(sts[j+1], null, null, ')'); j++;
		sts[j].addTransition(sts[j+1], null, null, 'T'); j++;
		sts[j].addTransition(sts[j+1], null, null, '('); j++;
		sts[j].addTransition(sts[j+1], null, null, 'f'); j++;
		sts[j].addTransition(qloop, null, null, 'i');
		
		qloop.addTransition(qloop, '(', '(', null);
		qloop.addTransition(qloop, ')', ')', null);
		qloop.addTransition(qloop, '{', '{', null);
		qloop.addTransition(qloop, '}', '}', null);
		qloop.addTransition(qloop, '=', '=', null);
		qloop.addTransition(qloop, '+', '+', null);
		qloop.addTransition(qloop, '*', '*', null);
		for (char c : a_z) qloop.addTransition(qloop, c, c, null);
		
		String tmp = Util.readFile("src/fonte.txt").trim();
		String w = "";
		for(Character c : tmp.toCharArray()) {
			if(c!=' ' && c!='\n' && c!='\r') 
				w = w + c;
		}
		// Aqui estamos usando o PDA da nossa biblioteca pda_automata_lib.jar
		// Tente implementar a sua PDAImpl.java
		// Apos isso, troque para: IPDA pda = new PDAImpl(qi);
		IPDA pda = new PDAImpl(qi);
		pda.makeLog();
		Util.checkout(pda.run(w),w);
		System.out.println("*****************************");
	}
	public static void se() throws Exception {
		char[] a_z = " abcdefghijklmnopqrstuvxywz".toCharArray();
		System.out.println("*****************************\nProcessamento de linguagem");
		IState q = new State("q");
		IState qi = new State("qi");
		IState qf = new State("qf");
		IState qOpenPar = new State("qOpenPar");
		IState t0 = new State("t0");
		IState t1 = new State("t1");
		IState t2 = new State("t2");
		IState t3 = new State("t3");
		IState t4 = new State("t4");
		IState qClosePar = new State("qClosePar");
		IState qOpenKey = new State("qOpenKey");
		IState qCloseKey = new State("qCloseKey");
		IState qif = new State("qif");
		List<IState> qs = Arrays.asList(q,qi,qf,qOpenPar,t0,t1,t2,t3,t4,qClosePar,qOpenKey,qCloseKey,qif);
		qif.setFinal();
		
		q.addTransition(qi, null, null, '$');
		qi.addTransition(qf, 'i', null, null);
		qf.addTransition(qOpenPar, 'f', null, null);
		qOpenPar.addTransition(t0, '(', null, null);
		t1.addTransition(t2, '=', null, null);
		t1.addTransition(qClosePar, ')', null, null);
		t2.addTransition(t3, '=', null, null);
		
		t4.addTransition(qClosePar, ')', null, null);
		
		qClosePar.addTransition(qOpenKey, '{', null, '{');
		qOpenKey.addTransition(qOpenKey, '{', null, '{');
		
		qOpenKey.addTransition(qCloseKey, '}', '{', null);
		qOpenKey.addTransition(qf, 'i', null, null);
		
		qCloseKey.addTransition(qCloseKey, '}', '{', null);
		qCloseKey.addTransition(qif, null, '$', null);
		
		for (char c : a_z) {
			t0.addTransition(t1, c, null, null);
			t1.addTransition(t1, c, null, null);
			t3.addTransition(t4, c, null, null);
			t4.addTransition(t4, c, null, null);
			qOpenKey.addTransition(qOpenKey, c, null, null); //para aceitar quarquer simbolo do alfabeto depois de {
		}
		for(IState s: qs) { 
			s.addTransition(s, '\n', null, null);
			s.addTransition(s, '\r', null, null);
		}
		// aceitar espacos
		qClosePar.addTransition(qClosePar, ' ', null, null);
		qOpenPar.addTransition(qOpenPar, ' ', null, null);
		qCloseKey.addTransition(qCloseKey, ' ', null, null);
		
		String w = Util.readFile("src/fonte.txt");
		
		// Aqui estamos usando o PDA da nossa biblioteca pda_automata_lib.jar
		// Tente implementar a sua PDAImpl.java
		// Apos isso, troque para: IPDA pda = new PDAImpl(q);
		IPDA pda = new PDA(q);
		//pda.makeLog();
		Util.checkout(pda.run(w),w);
		System.out.println("*****************************");
	}
	public static void reverso() throws Exception {
		/*
		 * L = { ww^R | w in Σ^*={0,1}}
		*/
		IState q1 = new State("q1");
		IState q2 = new State("q2");
		IState q3 = new State("q3");
		IState q4 = new State("q4");
		q1.setFinal(); 
		q4.setFinal();
		
		q1.addTransition(q2, null, null, '$');
		
		q2.addTransition(q2, '0', null, '0');
		q2.addTransition(q2, '1', null, '1');
		q2.addTransition(q3, null, null, null);
		
		q3.addTransition(q3, '0', '0', null);
		q3.addTransition(q3, '1', '1', null);
		
		q3.addTransition(q4, null, '$', null);
		
		String w = "111011110111";
		
		// Aqui estamos usando o PDA da nossa biblioteca pda_automata_lib.jar
		// Tente implementar a sua PDAImpl.java
		// Apos isso, troque para: IPDA pda = new PDAImpl(q1);
		IPDA pda = new PDA(q1);
		pda.makeLog();
		Util.checkout(pda.run(w),w);
		System.out.println("*****************************");
	}
}
