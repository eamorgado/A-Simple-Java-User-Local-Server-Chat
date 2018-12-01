import  java.util.*;
import java.util.Scanner;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
public class Cliente{
    private static int linhas(String ficheiro){int lines=0; //Acabado
      //Permite contar as linhas do ficheiro introduzido
      //Apenas o nome (diretóriopai/nome)
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ficheiro+".txt"));
            while(reader.readLine() != null) lines++;
            reader.close();
        }catch(IOException e){e.getStackTrace();}
        return lines;
    }
    private static void menuCliente(Scanner userin){new clearScreen(); // Acabado-menu de arranque
       System.out.println("**Menu de Registo**");
       System.out.println("1) Login / autenticação");
       System.out.println("2) Pedido de registo de novo utilizador");
       System.out.println("3) Sair");
       int escolha = userin.nextInt();
       if(escolha==3) System.exit(0); //Sair
       else if(escolha==2){registarUtilizador(userin);}//criar utilizador
       else if(escolha==1) {loginCliente(userin);}//login
    }
    private static void registarUtilizador(Scanner stdin){ // Acabado- Envia pedido de registo
       new clearScreen(); stdin.nextLine();
       System.out.print("Utilizador:");
       String util = stdin.nextLine();
       System.out.print("Password:"); String pass = stdin.nextLine();
       if(!verificarLogin(util,pass,2)){ //2º opcção, para registar, verifica se já existe algum utilizador com o  mesmo nome
           try {
                FileWriter fw = new FileWriter("Membros.txt",true); //escreve para o fim do ficheiro
                PrintWriter pw = new PrintWriter(fw);//permit esc
                pw.append(util+" "+pass+" NV\n");//escreve:  nomeUtilizador  Passwor    V(álido)/N(ão)V(álido)
                pw.close(); System.out.println("Utilizador registado com sucesso."); new Wait(1500); menuCliente(stdin);
            } catch (IOException e) {e.printStackTrace(); }
        } else {System.out.println("Utilizador já existe...Tente novamente"); new Wait(1500);menuCliente(stdin);}
    }
    private  static boolean verificarLogin(String util, String pass, int tipo){ // Acabado verifica se os utilizadors exi
       boolean encontrou = false;
       String tmpU="",tmpP="", tmpV="";
           try{
               Scanner stdin = new Scanner(new File("Membros.txt")); //ler de ficheiro de utilizadores e pass
              while(stdin.hasNext()){
                   tmpU = stdin.next(); tmpP=stdin.next(); tmpV= stdin.next(); //guarda valores de util,pass e estado de validade por linha
                   if(tipo == 1){ //verificar para LOGIN
                       if(tmpU.equals(util) && tmpP.equals(pass) && tmpV.equals("V")) {  stdin. close(); return true;}
                   } else{//verificar para REGISTO
                         if(tmpU.equals(util)) {  stdin. close(); return true;}//utilizador existe
                   }
               }
               stdin.close();
               return false; //utilizador n existe
           }catch(Exception e){return false;}
    }
    private static void loginCliente(Scanner stdin){ //Acabado
       new clearScreen(); stdin.nextLine();
       System.out.print("Utilizador:"); String util = stdin.nextLine();
       System.out.print("Password:"); String pass = stdin.nextLine();
       if(verificarLogin(util,pass,1)){System.out.println("Bem vindo/a " + util); new Wait(2000); menuClienteLogged(stdin,util);} //Utiizador existe? e pass tá corretaa? o utilizador está Válido?
       else {System.out.println("Utilizador ou password Errados, ou Inválidos...Tente novamente"); new Wait(1000); menuCliente(stdin);}
    }
    private static void menuClienteLogged(Scanner stdin, String util){//Acabado
       new clearScreen();
       System.out.println("**Menu**");
       System.out.println("1) Ver feed;"); //publicações recentes
       System.out.println("2) Ver tópicos;");
       System.out.println("3) Procurar tópicos mais ativos;");//contar linhas
       System.out.println("4) Subscrever tópico;");
       System.out.println("5) Publicar num tópico;");//nos que estão subs pro mim
       System.out.println("6) Gerir Lista de Subscrições;");
       System.out.println("7) Mudar password;");
       System.out.println("8) Ver estatíticas;"); //num de utilizadores...
       System.out.println("9) Cancelar subscrição a tópicos;");
       System.out.println("10) Logout;");
       int escolha = stdin.nextInt();
        switch (escolha){
            case 10: menuCliente(stdin); break; //logout-Ac
            case 1: verFeed(stdin,util); break;
            case 2: verTopicos(stdin, util); break;//Ac
            case 3: procurarTopicosMaisAtivos(stdin,util); break;//Ac
            case 4: subscreverTopico(stdin, util); break;//Ac
            case 5: publicarTopico(stdin,util); break;//Ac
            case 6: listaSubs(stdin,util); break;//Ac
            case 7: mudarPass(stdin,util); break;//Ac
            case 8: verEstatisticas(stdin,util); break;
            case 9: cancelarSub(stdin,util); break;
            default: menuClienteLogged(stdin,util); break;
        }
    }
    private static void cancelarSub(Scanner userin, String util){ //contrário ao subscrever
      new clearScreen(); if(linhas("Topicos")==0){System.out.println("Não existem tópicos"); new Wait(1000); menuClienteLogged(userin,util);}
      try {
          Scanner files = new Scanner(new File("Clientes/"+util+".txt")); //le ficheiro util
          int numTopic = linhas("Clientes/"+util);
          if(numTopic == 0) {System.out.println("Não tem nenhum tópico subscrito");
              new Wait(2000); menuClienteLogged(userin, util);
          }
          else{ boolean test=false;
              String[] topicosGerir = topicos();
              String topicosUtil[] = new String[numTopic];
              for(int i=0; i<numTopic; i++){topicosUtil[i]=files.nextLine();} files.close();
              //Filtro apenas aqueles que existem no utilizador.txt, com os indices no Topicos.txt corretos
              for(int i=0; i<linhas("Topicos"); i++){//percorro topicos todos
                  for(int k=0; k<numTopic; k++){//percorro todos do util
                      if(topicosGerir[i].equals(topicosUtil[k]))
                      System.out.println(i+") " + topicosGerir[i]);
                  }
              }
              System.out.println("\nQual escolhe? (-1 para voltar)"); int escolha= userin.nextInt();
              if(escolha == -1) menuClienteLogged(userin, util);
              else{ new clearScreen();
                  System.out.println("Escolheu \""+topicosGerir[escolha]+"\":\n...A Eliminar...");
                  try(BufferedWriter writer = new BufferedWriter( new FileWriter("Clientes/" + util +".txt"))){ //reescreve o ficheiro
                    for(int i=0; i<numTopic; i++){
                      if(!topicosUtil[i].equals(topicosGerir[escolha])) writer.write(topicosUtil[i]+"\n");
                    }
                    writer.close();
                  }catch(IOException e){e.getStackTrace();} menuClienteLogged(userin,util);

              }
          }
      }catch(IOException e){e.getStackTrace();}
    }
    private static void verEstatisticas(Scanner userin, String util){//Acabado
      new clearScreen();
      System.out.println("Membros: "+linhas("Membros"));
      try {
        Scanner stdin = new Scanner(new File("Membros.txt")); int mA=0;
        for(int i=0; stdin.hasNext(); i++) {
            stdin.next(); stdin.next(); if(stdin.next().equals("V")) mA++; //conta membros ativos
        } stdin.close();
        System.out.println("Membros ativos/validos: "+ mA);
        System.out.println("Membros inativos/não válidos: "+(linhas("Membros")-mA));
      }catch(Exception e){}
      System.out.println("Tópicos: "+linhas("Topicos")); int tmp=0;
        if(linhas("Topicos") == 0){
          System.out.println("\n-1 para voltar"); tmp=userin.nextInt();
          if(tmp==-1) menuClienteLogged(userin,util);
        }
        tmp=0; String[] tp=topicos();
        for(int i=0; i<linhas("Topicos");i++){
          if(linhas("Topicos/"+tp[i]) > tmp) tmp=linhas("Topicos/"+tp[i]);
        }
      System.out.println("Linhas de tópico mais ativo: "+tmp);
      System.out.println("-1 para voltar"); tmp=userin.nextInt();
      if(tmp==-1) menuClienteLogged(userin,util);
    }
    private static void verFeed(Scanner userin, String util){//Acabado
      new clearScreen(); if(linhas("Topicos")==0){System.out.println("Não existem tópicos"); new Wait(1000); menuClienteLogged(userin,util);}
      try { System.out.println("Nota,caso faltem tópicos, não existem atualizações");
        String[] tp = topicos(); String tmp1="",tmp2="", tmp3="";
        for(int i=0;i<linhas("Topicos"); i++){
            if(((linhas("Topicos/"+tp[i])-2)/3) > 0){
              Scanner stdin = new Scanner(new File("Topicos/"+tp[i]+".txt")); stdin.nextLine(); stdin.nextLine(); //Vai buscar as ultimas linhas de comemtário aos topicos
              for(int j=0; j<((linhas("Topicos/" + tp[i])-2)/3); j++){
                tmp1=stdin.nextLine(); tmp2=stdin.nextLine(); tmp3=stdin.nextLine();
              }
                stdin.close();
                System.out.println("\nEm \""+tp[i]+"\":");
                System.out.println(tmp1+"\n"+tmp2+"\n"+tmp3);
            }
        }
        System.out.println("\n\nPara voltar prima -1;\nPara atualizar feed, prima 1."); int tmp=userin.nextInt();
        if(tmp==-1) menuClienteLogged(userin,util);
        else if(tmp==1) verFeed(userin,util);
      }catch(Exception e){menuClienteLogged(userin,util);}
    }
    private static String[] topicos(){ String[] topicos2 = new String[linhas("Topicos")]; //Acabado
      //retorna lista ordenada de todos os tópicos existentes
        try{
            BufferedReader reader = new BufferedReader(new FileReader("Topicos.txt")); int i=0; String s="";
                while((s=reader.readLine()) != null ){topicos2[i] = s; i++;}
                reader.close();
        }catch(IOException e){e.getStackTrace();}
        return topicos2;
    }
    private static void verTopicos(Scanner userin, String util){//Acabado
      //mostra tds os topicos
        new clearScreen(); if(linhas("Topicos")==0){System.out.println("Não existem tópicos"); new Wait(1000); menuClienteLogged(userin,util);}
       String[] topicos = topicos();
            int i=0;
                while(i<linhas("Topicos")){System.out.println(i+") " + topicos[i]); i++;}
                System.out.println("Qual escolher? (-1 para sair)"); int escolha = userin.nextInt();
                if(escolha==-1) menuClienteLogged(userin, util);
                new clearScreen(); System.out.println("Selecionou \""+topicos[escolha]+"\"");
                System.out.println("\nO que fazer? \na) Subscrever \nb) Ler \nc) Ver os meus tópicos \nd) Voltar"); userin.nextLine();
                String esc = userin.nextLine();
            switch(esc){
                case "d": menuClienteLogged(userin, util); break;
                case "c": listaSubs(userin, util); break;
                case "b": lerTopico(userin, util,escolha); break;
                case "a": subscreverTopico(userin, util); break;
            }

    }
    private static void procurarTopicosMaisAtivos(Scanner userin, String util){//Acabado
      //conta as linhas de cada topico compara
        new clearScreen();
        if(linhas("Topicos")>=3){String[] tp = topicos(); int[] max = new int[linhas("Topicos")]; int[] ind = new int[linhas("Topicos")];
            System.out.println("Os tópicos apresentados estão ordenados pelo número de posts publicados;\n(Em caso de igualdade vêm por ordem de criação)");
            for(int i=0; i<linhas("Topicos");i++){max[i]=linhas("Topicos/"+tp[i]); ind[i]=i;}
            int tmp, tmpIn;
            for(int i=0; i<linhas("Topicos");i++){
                for(int j=i+1; j<linhas("Topicos");j++){
                        if(max[i]<max[j]){
                            tmp=max[j]; tmpIn=j;
                            max[j]=max[i]; ind[j]=i;
                            max[i]=tmp; ind[i]=tmpIn;
                        }
                }
            }
            for(int i=0; i<3;i++) System.out.print(ind[i]+") "+tp[ind[i]]+";\n");
            System.out.println("Qual escolher? (-1 para voltar)"); int escolha = userin.nextInt();
            if(escolha==-1) menuClienteLogged(userin,util);
            else{new clearScreen(); System.out.println("Escolheu \""+tp[escolha]+"\"\nO que fazer?\n1) Ler;\n2)Voltar;");
                    int esc = userin.nextInt();
                if(esc==1) lerTopico(userin,util,escolha);
                else procurarTopicosMaisAtivos(userin,util);
            }
        }
        else if(linhas("Topicos")==2){//Acabado
            String[] tp = topicos();
            System.out.println("Os tópicos apresentados estão ordenados pelo número de posts publicados;\n(Em caso de igualdade vêm por ordem de criação)");
            if(linhas("Topicos/"+tp[0])<linhas("Topicos/"+tp[1]))System.out.println("1) "+tp[1]+";\n0) "+tp[0]+";");
            else System.out.println("0) "+tp[0]+";\n1) "+tp[1]+";");
            System.out.println("Qual escolher? (-1 para voltar)"); int escolha = userin.nextInt();
            if(escolha==-1) menuClienteLogged(userin,util);
            else{new clearScreen(); System.out.println("Escolheu \""+tp[escolha]+"\"\nO que fazer?\n1) Ler;\n2)Voltar;");
                int esc = userin.nextInt();
                if(esc==1) lerTopico(userin,util,escolha);
                else procurarTopicosMaisAtivos(userin,util);
            }
        }
        else if(linhas("Topicos")==1){String[] tp = topicos(); //Acabado
            System.out.println("Apenas existe um único topico \""+tp[0]+"\"");
            System.out.println("O que fazer?\n1) Ler;\n2)Voltar;");
            int escolha = userin.nextInt();
            if(escolha==1) lerTopico(userin,util,0);
            else menuClienteLogged(userin,util);
        }
        else {System.out.println("Não existem tópicos"); new Wait(1500); menuClienteLogged(userin,util);}
    }
    private static void subscreverTopico(Scanner userin, String util){//Acabado
      //primeiro mostra apenas os tópicos que não se encontram no ficheiro utilizador.txt, por ordem
        new clearScreen();
        if(linhas("Topicos")==0){System.out.println("Não existem tópicos"); new Wait(1000); menuClienteLogged(userin,util);}
        if(linhas("Clientes/"+util) >7){System.out.println("Valor máximo de tópicos subscritos atingido"); new Wait(1500); menuClienteLogged(userin,util);}
        try { Scanner files = new Scanner(new File("Clientes/"+util+".txt")); //le ficheiro util
         int topNum= linhas("Clientes/"+util); //numero de topicos do utilixador
         String[] topicosGerir = topicos(); boolean test=false;
         if(topNum !=0) { System.out.println("Tópicos por subscrever:");
            String topicosUtil[] = new String[topNum];
            for(int i=0; i<topNum; i++){topicosUtil[i]=files.nextLine(); /*System.out.println(topicosUtil[i]);*/} files.close();
             for(int i=0; i<linhas("Topicos"); i++){//percorro topicos todos
                 for(int k=0; k<topNum; k++){//percorro todos do util
                     if(topicosGerir[i].equals(topicosUtil[k])) break;
                     if(k==topNum-1) {System.out.println(i+") " + topicosGerir[i]); test=true;}
                    }
                }
        } else{ System.out.println("Tópicos por subscrever:");
            for(int i=0; i<linhas("Topicos"); i++){System.out.println(i+") " + topicosGerir[i]); test=true;}
        }
         //if(test==false){menuClienteLogged(userin,util);}
         System.out.println("\nQual subscrever? (-1 para voltar) \nNota: Apenas aparecem topicos não subscritos"); int escolha= userin.nextInt();
         if(escolha == -1) menuClienteLogged(userin, util);
         else{
             FileWriter fw = new FileWriter("Clientes/" + util + ".txt",true); //escreve para o fim do ficheiro
             PrintWriter pw = new PrintWriter(fw);
                 pw.append(topicosGerir[escolha]+"\n");//escreve no tópico
                 pw.close();
            }
        }catch(IOException e) {e.getStackTrace();} menuClienteLogged(userin,util);
    }
    private static void publicarTopico(Scanner userin, String util){//Acabado
      //vê se o utilizador tem esse tópico subscrito
        new clearScreen(); if(linhas("Topicos")==0){System.out.println("Não existem tópicos"); new Wait(1000); menuClienteLogged(userin,util);}
        try {
            Scanner files = new Scanner(new File("Clientes/"+util+".txt")); //le ficheiro util
            int numTopic = linhas("Clientes/"+util);
            if(numTopic==0) {System.out.println("Não tem nenhum tópico subscrito"); new Wait(2000); menuClienteLogged(userin, util);}
            else{String[] topicosGerir = topicos();
                System.out.println("Os seus tópicos (só pode publicar se subscrever):");
                String topicosUtil[] = new String[numTopic]; boolean test=false;
            for(int i=0; i<numTopic; i++){topicosUtil[i]=files.nextLine(); /*System.out.println(topicosUtil[i]);*/} files.close();
                for(int i=0; i<linhas("Topicos"); i++){//percorro topicos todos
                    for(int k=0; k<numTopic; k++){//percorro todos do util
                        if(topicosGerir[i].equals(topicosUtil[k]))
                        System.out.println(i+") " + topicosGerir[i]);
                    }
                } //if(test==false){menuClienteLogged(userin,util);}
                System.out.println("Selecione o tópico onde publicar (-1 para sair)"); int escolha=userin.nextInt();
                if(escolha==-1){menuClienteLogged(userin, util);}
                else{escreverTopico(userin,util,escolha);}
            }
        }catch(IOException e){e.getStackTrace();}
    }
    private static void listaSubs(Scanner userin, String util){//Acabado
        new clearScreen();
        try {
            Scanner files = new Scanner(new File("Clientes/"+util+".txt")); //le ficheiro util
            int numTopic = linhas("Clientes/"+util);
            if(numTopic == 0) {System.out.println("Não tem nenhum tópico subscrito");
                new Wait(2000); menuClienteLogged(userin, util);
            }
            else{ boolean test=false;
                String[] topicosGerir = topicos();
                String topicosUtil[] = new String[numTopic];
                for(int i=0; i<numTopic; i++){topicosUtil[i]=files.nextLine(); } files.close();
                for(int i=0; i<linhas("Topicos"); i++){//percorro topicos todos
                    for(int k=0; k<numTopic; k++){//percorro todos do util
                        if(topicosGerir[i].equals(topicosUtil[k]))
                        System.out.println(i+") " + topicosGerir[i]);
                    }
                }
                System.out.println("\nQual escolhe? (-1 para voltar)"); int escolha= userin.nextInt();
                if(escolha == -1) menuClienteLogged(userin, util);
                else{ new clearScreen();
                    System.out.println("Escolheu \""+topicosGerir[escolha]+"\":");
                    System.out.println("a)Ler;\nb)Escrever;\nc)Voltar;"); for(;;){String esc=userin.nextLine();
                    switch (esc){
                        case "a": lerTopico(userin, util,escolha);
                        case "b": escreverTopico(userin,util,escolha);
                        case "c": menuClienteLogged(userin,util);
                    }}
                }
            }
        }catch(IOException e){e.getStackTrace();}
    }
    private static void lerTopico(Scanner userin, String util, int escolha){//Acabado com gostos
        new clearScreen();
        try{ String[] topicos = topicos(); String titul=""; String[][] str = new String[(linhas("Topicos/" + topicos[escolha])-2)/3][3]; //para ignorar as duas primmeiras linhas comuns a todos os topicos
          if(((linhas("Topicos/" + topicos[escolha])-2)/3) <= 0){String t="";
            BufferedReader readerT = new BufferedReader(new FileReader("Topicos/" + topicos[escolha] +".txt")); //imprime o topico
            while((t=readerT.readLine()) != null)System.out.println(t);  readerT.close(); System.out.println("\n"); System.out.println("\nTópicos sem actividade");
            new Wait(1000); menuClienteLogged(userin,util);
          }
          else{ //caso tenha publicações
            Scanner file = new Scanner(new File("Topicos/" + topicos[escolha] +".txt")); String tmp=file.nextLine();
            System.out.println(tmp); System.out.println(file.nextLine());
            for(int i=0; i<((linhas("Topicos/" + topicos[escolha])-2)/3); i++){
              str[i][0]=file.nextLine(); //nome do utilizador-data e hora da publicação
              str[i][1]=file.nextLine();//a propria publicaçao
              str[i][2]=file.nextLine();//os gostos

              System.out.println(i+") "+str[i][0]+"\n"+str[i][1]+"\n             "+str[i][2]+"\n");
            }
            System.out.println("\nSelecione o comentário a gostar(-1 para voltar)");
            int esc = userin.nextInt();
            if(esc==-1) menuClienteLogged(userin,util);
            else{Scanner tp = new Scanner(str[esc][2]); //lê da parate dos gostos
              String tmp1=tp.next();tp.close(); //retira apenas o numero de gostos
              int gostos = Integer.parseInt(tmp1) + 1, siz=((linhas("Topicos/" + topicos[escolha])-2)/3); // converte o numero de gostos para inteiro |linhas do ficheiro de topico atual, apenas com comentários
              str[esc][2]=""+gostos+" gostos";
              try(BufferedWriter writer = new BufferedWriter( new FileWriter("Topicos/" + topicos[escolha] +".txt"))){
                writer.write(tmp); writer.write("\n\n");
                for(int i=0; i<siz; i++){writer.write(str[i][0]+"\n"+str[i][1]+"\n"+str[i][2]);} //reescreve todo o ficheiro, já com os gostos atualizados
                writer.close();
              }catch(IOException e){e.getStackTrace();} menuClienteLogged(userin,util);
            }
          }
        }catch(IOException e){e.getStackTrace();}
    }
    private static void escreverTopico(Scanner userin, String util, int escolha){//Acabado
        System.out.println("\nPara sair digite -1\nEscreva:");
        String texto="";
        ArrayList<String> list = new ArrayList<String>();
        for(;;){texto = userin.nextLine();
            if(texto.equals("-1")) break; //{list.add(texto);break;}
            list.add(texto);
        } if(list.size() == 1) {new clearScreen();menuClienteLogged(userin, util);} //vê se só tem umaúnica linha "-1" se assim for ignora
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //formato para data
                SimpleDateFormat t = new SimpleDateFormat("HH:mm:ss"); //formato para hora
                String date = sdf.format(new Date());
                Calendar cal = Calendar.getInstance();
                cal.getTime(); String time = t.format(cal.getTime()); //guarda data e hora do momento em que o post foi publicado/feito
                String[] topicosGerir = topicos(); //vai buscar o array de topicos existente
                FileWriter fw = new FileWriter("Topicos/" +  topicosGerir[escolha] + ".txt",true); //escreve para o fim do ficheiro
                PrintWriter pw = new PrintWriter(fw);
                pw.append("\n"+util+" em "+date+ " às "+ time +" escreveu:\n    ");//escreve no tópico
                for(int j=0; j<list.size(); j++) pw.append(" "+list.get(j));
                pw.append("\n0 gostos");
                pw.close();//fecha a escrita, permite escrever ao mesmo tempo
                menuClienteLogged(userin,util);
            }catch(IOException e){e.getStackTrace();}
                menuClienteLogged(userin,util);
    }
    private static void mudarPass(Scanner userin, String util){//Acabado
      new clearScreen(); int size=linhas("Membros"),numU=0;;
      String[][] utilizadores = new String[size][3]; //cria a matriz com coluna Util Pass Validade
      //Processo igual a mudar o estdo de validade no Servidor.java
      try {
          Scanner stdin = new Scanner(new File("Membros.txt"));
          for(int i=0; stdin.hasNext(); i++) {utilizadores[i][0]=stdin.next(); utilizadores[i][1]=stdin.next(); utilizadores[i][2]=stdin.next();}
           stdin.close();
      }catch(Exception e){menuClienteLogged(userin,util);}

        for(int i=0; i<size;i++){if(utilizadores[i][0].equals(util)){numU=i; break;}}
        userin.nextLine(); System.out.println("A sua password atual é \""+utilizadores[numU][1]+"\" mudar para: ");// int tmp=userin.nextInt();
        //if(tmp==-1) menuClienteLogged(userin,util);

        String pass=userin.nextLine();
        if(!pass.equals(utilizadores[numU][1])){ utilizadores[numU][1]=pass;}
        else{System.out.println("A nova palavra pass não pode ser igual à anterior."); new Wait(1000); menuClienteLogged(userin,util);}

        try(BufferedWriter writer = new BufferedWriter( new FileWriter("Membros.txt"))){
          for(int i=0; i<size; i++){writer.write(utilizadores[i][0] + " " + utilizadores[i][1] + " " + utilizadores[i][2]+"\n");}
          writer.close();
        }catch(IOException e){e.getStackTrace();}
        menuClienteLogged(userin,util);
    }
    public static void main(String[] args) {Scanner userin = new Scanner(System.in);
        menuCliente(userin);
    }
}
