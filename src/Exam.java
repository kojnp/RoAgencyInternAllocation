import java.io.*;
import java.util.*;

public class Exam {
	
	public static int NUM_AGENCIES = 16;

	class Candidate{
		String email;
		float medie;
		int notaexam1;
		int notaexam2;
		String[] optiuni;
		String agentiaLaCareAFostAlocat;
		public Candidate(String email, int notaexam1, int notaexam2, float medie, String[] optiuni){
		
			this.email=email;
			this.notaexam1=notaexam1;
			this.notaexam2=notaexam2;
			this.medie=medie;
			this.optiuni=optiuni;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public float getMedie() {
			return medie;
		}

		public void setMedie(float medie) {
			this.medie = medie;
		}

		public int getNotaexam1() {
			return notaexam1;
		}

		public void setNotaexam1(int notaexam1) {
			this.notaexam1 = notaexam1;
		}

		public int getNotaexam2() {
			return notaexam2;
		}

		public void setNotaexam2(int notaexam2) {
			this.notaexam2 = notaexam2;
		}

		public String[] getOptiuni() {
			return optiuni;
		}

		public void setOptiuni(String[] optiuni) {
			this.optiuni = optiuni;
		}

		@Override
		public boolean equals(Object obj) {
			return ((Candidate)obj).getEmail().equals(this.getEmail()) ? true : false;
		}
		@Override
		public int hashCode() {
			return email.hashCode();
		}

		public String getAgentiaLaCareAFostAlocat() {
			return agentiaLaCareAFostAlocat;
		}

		public void setAgentiaLaCareAFostAlocat(String agentiaLaCareAFostAlocat) {
			this.agentiaLaCareAFostAlocat = agentiaLaCareAFostAlocat;
		}
		
	}
	
	class Agency{
		String name;
		int preferenceScore;
		List<Candidate> interns = new ArrayList<Candidate>();
		
		public Agency(String name){
		
			this.name=name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getPreferenceScore() {
			return preferenceScore;
		}
		public void setPreferenceScore(int preferenceScore) {
			this.preferenceScore = preferenceScore;
		}
		@Override
		public boolean equals(Object obj) {
			return ((Agency)obj).getName().equals(this.getName()) ? true : false;
		}
		@Override
		public int hashCode() {
			return name.hashCode();
		}
		public List<Candidate> getInterns() {
			return interns;
		}
		public void setInterns(List<Candidate> interns) {
			this.interns = interns;
		}		
		 
	}
	
	List<Candidate> candidates;
	List<Agency> agencies;
	
	public void readAgenciesFromFile(String inputFile) {
        String line = "";
        String cvsSplitBy = ",";
        agencies = new ArrayList<Agency>();
        Exam.printSep();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {

            line = br.readLine();
            
            // use comma as separator
            String[] tokens = line.split(cvsSplitBy);

            for (String token:tokens) {
                System.out.println(token);
            		agencies.add(new Agency(token));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/** also has side effect of computing agency preference score...
	 * */ 
	public void readCandidatesFromFile(String inputFile) {
 
        String line = "";
        String cvsSplitBy = ",";
        candidates = new ArrayList<Candidate>();
        Exam.printSep();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {

        		line = br.readLine();// skip header like
        		System.out.println("header=" + line);
        		
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] tokens = line.split(cvsSplitBy);
                Exam.printSep();

                System.out.println(line);
                String email = tokens[1];
                String notaexam1 = tokens[2];
                String notaexam2 = tokens[3];
                String medie = tokens[20];
                String pozitie = tokens[21];
                
                String[] optiuni = new String[NUM_AGENCIES];
                System.arraycopy(tokens, 4, optiuni, 0, NUM_AGENCIES);
                
                System.out.println(email+ " " + notaexam1 + " , " + notaexam2 +
                		" , medie=" + medie + " ; pozitie = " + pozitie);
                
                for (int i = 0; i < optiuni.length; i++) {
                		String opt = optiuni[i];
                		Agency a = agencies.get(agencies.indexOf(new Agency(opt)));
                		
                		System.out.println(opt+" position in candidate list of preferences =" + i + " current pref =" + a.getPreferenceScore());
                		a.setPreferenceScore(a.getPreferenceScore()+NUM_AGENCIES-i);
                		System.out.println(opt+" pref increased to " + a.getPreferenceScore());
                }
                
                candidates.add(new Candidate(email,Integer.parseInt(notaexam1), Integer.parseInt(notaexam1), 
                		Float.parseFloat(medie),optiuni));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	public Exam(String agenciesInputFile, String candidatesInputFile) {
		readAgenciesFromFile(agenciesInputFile);
		readCandidatesFromFile(candidatesInputFile);
		
		agencies.sort(Comparator.comparing(Agency::getPreferenceScore).reversed());
		Exam.printSep();
		for (Agency a : agencies){
			System.out.println(a.getName() + " pe locul " + (agencies.indexOf(a)+1) + " scorul de preferinta " + a.getPreferenceScore());
		}
		
		candidates.sort(Comparator.comparing(Candidate::getMedie).reversed());
		Exam.printSep();
		for (Candidate c : candidates){
			System.out.println(c.getEmail() + " pe locul " + (candidates.indexOf(c)+1) + " media " + c.getMedie());
		}
		
		/*
		int candidateListIndex=0;
		for (int i = 0; i < 10; i++) {
			Exam.printSep();
			System.out.println("Candidatii repartizati la agentia " + agencies.get(i).getName() + " sunt :");
			for (int j=0; j<3; j++) {
				System.out.println(candidates.get(candidateListIndex).getEmail());
				candidateListIndex++;
			}
		}
		for (int i = 11; i < 16; i++) {
			Exam.printSep();
			System.out.println("Candidatii repartizati la agentia " + agencies.get(i).getName() + " sunt :");
			for (int j=0; j<2; j++) {
				System.out.println(candidates.get(candidateListIndex).getEmail());
				candidateListIndex++;
			}
		}
		*/
		// primii 30 sunt repartizati unde vor, in ordinea preferintelor, avand in vedere sa fie max 3 per agentie.
		// ultimii 12 sunt repartizati unde vor, in ordinea preferintelor, avand in vedere sa fie max 2 per agentie. 
		// Deci niciunul din ultimii 12 candidati nu vor fi repartizati la agentii unde sunt deja 2 repartizati. 
		Exam.printSep();
		allocateCandidates(30, 0, 3);
		allocateCandidates(12, 30, 2);


		Exam.printSep();
		for (Candidate c : candidates){
			System.out.println(c.getEmail() + " pe locul " + (candidates.indexOf(c)+1) + " media " + c.getMedie());
		}
		Exam.printSep();
		for (Agency a : agencies){
			System.out.println(a.getName() + " pe locul " + (agencies.indexOf(a)+1) + " scorul de preferinta " + a.getPreferenceScore());
		}
		Exam.printSep();
		
		System.out.println("Repartizare finala");
		System.out.println("primii 30 ca medie sunt repartizati unde vor, in ordinea preferintelor lor, \n avand in vedere sa fie max 3 per agentie.\n" + 
				"		ultimii 12 sunt repartizati unde vor, in ordinea preferintelor, avand in vedere sa fie max 2 per agentie. \n" + 
				"	Deci niciunul din ultimii 12 candidati ca medie nu au fost repartizati la agentii unde sunt deja 2 repartizati. ");
		Exam.printSep();
		
		for (Agency a : agencies) {
			Exam.printSep();
			System.out.println("Agentia " + a.getName());
			for (Candidate c : a.getInterns()) {
				System.out.println(c.getEmail());
			}
		}
	}

	private void allocateCandidates(int maxCandidatesThisStage, int startCandidatesFromIndex, int maxInternsAllowedPerAgency) {
		for (int i = startCandidatesFromIndex + 0; i < (startCandidatesFromIndex + maxCandidatesThisStage); i++) {// iterare in lista sortata deja descendent dupa punctaj
			
			Exam.printSep();
			System.out.println("Algoritm Repartizare Candidat " + candidates.get(i).getEmail());

			String[] optiuni = candidates.get(i).getOptiuni();
			for (int j = 0 ; j < 16 ; j++) {
				System.out.println("optiunea " + j + " a fost " + optiuni[j]);
				Agency a = agencies.get(agencies.indexOf(new Agency(optiuni[j])));
				System.out.println("agentia " + a.getName() + " are acum " + a.getInterns().size() + " interni deja repartizati");
				if (a.getInterns().size() < maxInternsAllowedPerAgency) {
					System.out.println("Il alocam la agentia " + a.getName());
					a.interns.add(candidates.get(i));
					candidates.get(i).setAgentiaLaCareAFostAlocat(optiuni[j]);
					break;
				}
			}
		}
	}

	public static void main(String[] args) {
		// 42 candidati, 16 agentii. min 2, max 3 repartizati la fiecare
		// 16x2=32 candidati. raman 10 sa fie pusi la primele 10 agentii
		// primele 10 agentii primesc cate 3 candidati => 10x3=30, raman 12 pt urmatoarele 6, in ordinea celor mai dorite
		// DAR nu toti candidatii impartasesc clasamentul final al agentiilor - tb sa mers in ordinea mediilor si a preferintelor pt fiecare candidat
		System.out.println("input file=" + args[0] + " and " + args[1]);
		Exam e = new Exam(args[0], args[1]);
	}
	
	public static void printSep() {
		System.out.println("===========================================================");
	}

}
