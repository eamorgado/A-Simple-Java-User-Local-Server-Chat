import java.util.Scanner;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
//import java.io.*; 

public class Servidor{
    private static final String Utilizador = "Admin"; //utilizadorServidor- permanente
    private static final String Password = "server"; //PassServidor- permanente
    private static int linhas(String ficheiro){int lines=0; //Acabado
      //conta linhas de ficheiro, igual ao linhas() em Cliente.java
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ficheiro+".txt"));
            while(reader.readLine() != null) lines++;
            reader.close();
        }catch(IOException e){e.getStackTrace();}
        return lines;
    }
    private static void menuServidor(){new clearScreen(); //Acabado
      //Menu de início apenas imprime---é o ecrã
        System.out.println("**Menu de Registo**");
        System.out.println("1) Faca login/autenticacao");
        System.out.println("2) Sair");
    }
    private static void menuArranque(Scanner userin){//Acabado
      //para escolha, podia ter sido "fundido " com o menuServidor
        menuServidor();  int escolha = userin.nextInt();
        if(escolha==1) {new clearScreen(); loginServidor(userin);}
        else if(escolha == 2) System.exit(0);
    }
    private static void loginServidor(Scanner stdin){//Acabado
      //apenas compara os valores de utilizador e pass com os finais nas linhas 13 e 14
        stdin.nextLine();
        System.out.print("Utilizador:");
        String util = stdin.nextLine(); //stdin.nextLine();
        //System.out.println(util);
        System.out.print("Password:"); String pass = stdin.nextLine();
        if(util.equals(Utilizador) && pass.equals(Password)) {menuServidorLogged(stdin);}
        else {System.out.println("Login ou senha inválidos!"); loginServidor(stdin);}
    }
    private static void menuServidorLogged(Scanner userin){//Acabado
        new clearScreen();
        System.out.println("**Menu**");
        System.out.println("1) Criar Novos tópicos");
        System.out.println("2) Gerir Tópicos"); //ler,escrever
        System.out.println("3) Gerir Utilizadores");//mudar estado de validade, ver os seus topicos subscritos
        System.out.println("4) Ver Estatísticas");//igual em Cliente.java
        System.out.println("5) Registar Utilizador");//igual em Cliente.java
        System.out.println("6) Logout");

        int escolha = userin.nextInt();
        switch (escolha) {//seleção
            case 1: criarTopico(userin); break; //Ac
            case 2: new clearScreen(); gerirTopicos(userin); break; //Ac
            case 3: gerirUtilizadores(userin); break;//Ac
            case 4: verEstatisticas(userin); break;//Ac
            case 6: menuArranque(userin); break;//Ac
            case 5: registarUtilizador(userin);
            default: System.out.println("Escolha inválida"); break;
        }
    }
    private static void registarUtilizador(Scanner stdin){ // Acabado- Envia pedido
      //igual em Cliente.java
       new clearScreen(); stdin.nextLine();
       System.out.print("Utilizador:");
       String util = stdin.nextLine(); //stdin.nextLine();
       //System.out.println(util);
       System.out.print("Password:"); String pass = stdin.nextLine();
       if(!verificarLogin(util,pass,2)){ //2º opcção, para registar, verifica se já existe algum utilizador com o  mesmo nome
           try {
                FileWriter fw = new FileWriter("Membros.txt",true); //escreve para o fim do ficheiro
                PrintWriter pw = new PrintWriter(fw);
                pw.append(util+" "+pass+" NV\n");//utilizador,passwor,(numeroPostSubscritos,TipodeUtilizador,)Válido/NãoVálido
                pw.close(); System.out.println("Utilizador registado com sucesso."); new Wait(1500); menuServidorLogged(stdin);
            } catch (IOException e) {e.printStackTrace(); }
        } else {System.out.println("Utilizador já existe...Tente novamente"); new Wait(1500);menuServidorLogged(stdin);}
    }
    private  static boolean verificarLogin(String util, String pass, int tipo){ // Acabado verifica se os utilizadors exi
      //igual em Cliente.java, neste caso será sempre o tipo 2 xp
       boolean encontrou = false;
       String tmpU="",tmpP="", tmpV="";
           try{
               Scanner stdin = new Scanner(new File("Membros.txt")); //ler de ficheiro de utilizadores e pass
               //stdin.useDelimiter("[\n]");//para parar de ler na , e \n

               while(stdin.hasNext()){
                   tmpU = stdin.next(); tmpP=stdin.next(); tmpV= stdin.next(); //System.out.println(tmpU + " " + tmpP + " " + tmpV);
                   if(tipo == 1){ //verificar para login
                       if(tmpU.equals(util) && tmpP.equals(pass) && tmpV.equals("V")) {  stdin. close(); return true;}
                   } else{//verificar para registo
                         if(tmpU.equals(util)) {  stdin. close(); return true;}//utilizador existe
                   }
                     //tmpU=stdin.next(); //tmpU=stdin.next(); tmpU=stdin.next();
               }
               stdin.close();
               return false; //utilizador n existe
           }catch(Exception e){//System.out.print("Erro");
            return false;}

    }
    private static void criarTopico(Scanner userin){new clearScreen(); //Acabado
      //similar ao registar utilizador
        userin.nextLine();  System.out.print("Nome de novo tópico: ");
        String topico = userin.nextLine(), s=""; //obtem o nome do topico
        try{
            BufferedReader reader = new BufferedReader(new FileReader("Topicos.txt"));
                while((s = reader.readLine()) !=null) {
                    if(s.equals(topico)){System.out.println("Tópico existe"); new Wait(2000); menuServidorLogged(userin);} //verifica se esse nome não existe
                } reader.close();
                //Não existe vamos criar-----
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");//para escrever no ficheiro novoTopico.txt-- data
                SimpleDateFormat t = new SimpleDateFormat("HH:mm:ss");//para escrever no ficheiro novoTopico.txt-- hora
                String date = sdf.format(new Date());
                Calendar cal = Calendar.getInstance();
                cal.getTime(); String time = t.format(cal.getTime());

            FileWriter fw = new FileWriter("Topicos/" + topico + ".txt",true); //para escrever no ficheiro novoTopico.txt
            FileWriter fwT = new FileWriter("Topicos.txt", true);//escreve para o fim do ficheiro Topicos.txt
                PrintWriter pwT = new PrintWriter(fwT);//escreve para o fim do ficheiro Topicos.txt
                PrintWriter pw = new PrintWriter(fw);//para escrever no ficheiro novoTopico.txt
                pw.append("Tópico Aberto em "+date+ " às "+ time +"\n");//escreve no tópico
                pw.close();
            pwT.append(topico+"\n"); //escreve no ficheiro de topicos
            pwT.close();
        } catch (IOException e) {e.printStackTrace(); System.out.println("...Erro..."); new Wait(1000); }
        menuServidorLogged(userin);
    }
    private static void  gerirTopicos(Scanner userin) {//Acabado
        System.out.println("Gerir tópicos: "); String[] topicosGerir = topicos();
        for(int i=0; i<linhas("Topicos");i++)System.out.println(i+") "+topicosGerir[i]); //apresenta por ordem todos os topicos
         System.out.println("Qual gerir? (-1 para voltar)"); int escolha = userin.nextInt();

            if(escolha==-1) menuServidorLogged(userin);
            else if(escolha<0 || escolha > linhas("Topicos")){System.out.println("...Erro..."); new Wait(1000); new clearScreen(); gerirTopicos(userin);}
            else{ new clearScreen(); System.out.println("Escolheu o tópico \"" + topicosGerir[escolha]+ "\"" );
              System.out.println("1) Ler"); //parecido ao em Cliente.java, sem gostos
              System.out.println("2) Escrever");//igial ao Cliente.java, só o utilizador é que é permanentemente Admin
              System.out.println("3) Voltar");
               int esc = userin.nextInt();
               switch(esc){
                   case 3: menuServidorLogged(userin); break;//Acabado LOL
                   case 2://Acabado
                   new clearScreen();
                   System.out.println("\nPara sair digite -1\nEscreva:");
                   String texto="";
                   ArrayList<String> list = new ArrayList<String>();
                   for(;;){texto = userin.nextLine();
                       if(texto.equals("-1")) break; //{list.add(texto);break;}
                       list.add(texto);
                   } if(list.size() == 1) {menuServidorLogged(userin);}
                       try {
                           SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                           SimpleDateFormat t = new SimpleDateFormat("HH:mm:ss");
                           String date = sdf.format(new Date());
                           Calendar cal = Calendar.getInstance();
                           cal.getTime(); String time = t.format(cal.getTime());
                           FileWriter fw = new FileWriter("Topicos/" +  topicosGerir[escolha] + ".txt",true); //escreve para o fim do ficheiro
                           PrintWriter pw = new PrintWriter(fw);
                           pw.append("\nAdmin em "+date+ " às "+ time +" escreveu:\n    ");//escreve no tópico
                           for(int j=0; j<list.size(); j++) pw.append(" "+list.get(j));
                           pw.append("\n0 gostos");
                           pw.close();
                           menuServidorLogged(userin);
                       }catch(IOException e){e.getStackTrace();}
                   break;
                   case 1: //Acabado
                        try{new clearScreen(); //é uma leitura seguida de impressão simples
                            BufferedReader readerT = new BufferedReader(new FileReader("Topicos/" + topicosGerir[escolha] +".txt"));
                            String t="";
                            while((t=readerT.readLine()) != null){System.out.println(t);} readerT.close(); System.out.println("\n");
                            System.out.println("\n -1 para sair"); String es = userin.nextLine();
                            if(es.equals("-1")) {new clearScreen(); gerirTopicos(userin);}
                            else{while(!es.equals("-1")){System.out.println("Tente novamente"); es = userin.nextLine();} new clearScreen();gerirTopicos(userin);}
                        }catch(IOException e){e.getStackTrace();}
                   break;
                  default: new clearScreen(); gerirTopicos(userin);
               }
           }

    }
    private static void gerirUtilizadores(Scanner userin){new clearScreen(); //Acabado
        String[][] utilizadores = new String[linhas("Membros")][3]; //3 colunas Utilizador | Pass | Estado de Validade
        try {
            Scanner stdin = new Scanner(new File("Membros.txt"));
            for(int i=0; stdin.hasNext(); i++) {
                utilizadores[i][0]=stdin.next(); utilizadores[i][1]=stdin.next(); utilizadores[i][2]=stdin.next();
                System.out.println(i+") " + utilizadores[i][0] + " " + utilizadores[i][1] + " " + utilizadores[i][2]);
            } stdin.close();
            System.out.println("Qual gerir? (-1 para sair)");
            int escolha = userin.nextInt();
            if(escolha == -1){menuServidorLogged(userin);}
            else{
                    for(int i=0; i<linhas("Membros"); i++){
                        for(int k=0; k<3; k++){ System.out.print(utilizadores[i][k]+ " ");} System.out.println();
                    } gerirUtil(userin, utilizadores, escolha, linhas("Membros")); //guarda a escolha, linhas é a quantidade de utilizadores
                }
        }catch(Exception e){System.out.println("...Erro..."); new Wait(2000); menuServidorLogged(userin);}
    }
    private static void gerirUtil(Scanner userin, String[][] utilizadores, int k, int sizeMat){new clearScreen(); //Acabado
        System.out.println("A gerir: " + utilizadores[k][0]);
        System.out.println("Gerir: " /*1) Estado de Validade;*/);
        System.out.println("1) Estado de validade;"); //Altera o estado de validade
        System.out.println("2) Ver tópicos subscritos;");//Abre o ficheiro utilizador.txt impime tudo
        System.out.println("3) Voltar a gerir utilizadores;");
        int escolha=userin.nextInt();
        switch (escolha) {
            case 3: gerirUtilizadores(userin); break;
            case 2: new clearScreen();
            try { //Apenas mostra os tópicos subscritos pelo utilizador
                Scanner files = new Scanner(new File("Clientes/"+utilizadores[k][0]+".txt")); //le ficheiro util.txt
                String numTopic = userin.nextLine();
                if(linhas("Clientes/"+utilizadores[k][0])==0) {System.out.println("Não tem nenhum tópico subscrito"); new Wait(2000); gerirUtil(userin, utilizadores, k, sizeMat);}
                else{int i=0; files.nextLine();
                    while(files.hasNextLine()){System.out.println(i+") " + files.nextLine()); i++;}
                    new Wait(3000); gerirUtil(userin, utilizadores, k, sizeMat);
                }
            }catch(IOException e){e.getStackTrace();}
            break;
            case 1:
                userin.nextLine();System.out.print(utilizadores[k][0]+" está "+utilizadores[k][2]+" alterar para(V/NV): ");
                String tmpVal = userin.nextLine(); //guarda o novo estado de validade
                    if(!tmpVal.equals(utilizadores[k][2])){ utilizadores[k][2] = tmpVal;}//guarda o novo valor se este for diferente do primeiro estado de validade
                    //Alterar o estado de validade do utilizador
                    try(BufferedWriter writer = new BufferedWriter( new FileWriter("Membros.txt"))){ //volta a reescrever (escrever por cima) o ficheiro Membros.txt como, já com o valor de utilizadores[k][2] atualizado
                        for(int i=0; i<sizeMat; i++){writer.write(utilizadores[i][0] + " " + utilizadores[i][1] + " " + utilizadores[i][2]+"\n");}
                        writer.close();
                    }catch(IOException e) {e.getStackTrace();}

                    if(utilizadores[k][2].equals("V")){ //depois do utilizador ser criado, ele está NV até o Servidor alterar, nesse primeiro momento, é criado o ficheiro Clientes/utilizador.txt
                        try{File permfile = new File("Clientes/"+ utilizadores[k][0] + ".txt"); //criar o ficheiro para o utilizador
                                permfile.createNewFile();
                        }catch (IOException e){System.out.println("Falha");}
                    }
                    gerirUtil(userin, utilizadores, escolha, linhas("Membros"));
            break;
        }
    }
    private static String[] topicos(){ String[] topicos2 = new String[linhas("Topicos")]; //Igual ao em Cliente.java
        try{
            BufferedReader reader = new BufferedReader(new FileReader("Topicos.txt")); int i=0; String s="";
                while((s=reader.readLine()) != null ){topicos2[i] = s; i++;}
                reader.close();
        }catch(IOException e){e.getStackTrace();}
        return topicos2;
    }
    private static void verEstatisticas(Scanner userin){//igual ao Cliente.java
      new clearScreen();
      System.out.println("Membros: "+linhas("Membros"));
      try {
        Scanner stdin = new Scanner(new File("Membros.txt")); int mA=0;
        for(int i=0; stdin.hasNext(); i++) {
            stdin.next(); stdin.next(); if(stdin.next().equals("V")) mA++;
        } stdin.close();
        System.out.println("Membros ativos/validos: "+ mA);
        System.out.println("Membros inativos/não válidos: "+(linhas("Membros")-mA));
      }catch(Exception e){}
      System.out.println("Tópicos: "+linhas("Topicos")); int tmp=0;
        if(linhas("Topicos") == 0){
          System.out.println("\n-1 para voltar"); tmp=userin.nextInt();
          if(tmp==-1) menuServidorLogged(userin);
        }
        tmp=0; String[] tp=topicos();
        for(int i=0; i<linhas("Topicos");i++){
          if(linhas("Topicos/"+tp[i]) > tmp) tmp=linhas("Topicos/"+tp[i]);
        }
      System.out.println("Linhas de tópico mais ativo: "+tmp);
      System.out.println("-1 para voltar"); tmp=userin.nextInt();
      if(tmp==-1) menuServidorLogged(userin);
    }
    public static void main(String[] args) {Scanner userin = new Scanner(System.in);   menuArranque(userin);}
}
