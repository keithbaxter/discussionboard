import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.io.*;

/**
   A simulated discussion board.
*/
public class DiscussionBoard
{
   private ArrayList<User> users;
   private ArrayList<Message> messages;
   private User currentUser;
   private Message currentMessage;
   private Scanner in;
   private PasswordEncryptionService p;
   private byte[] salt;
   private File usersFile;

   public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
	{
      new DiscussionBoard().run();
   }

   /**
      Constructs the discussion board.
   */
   public DiscussionBoard() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
   {
      in = new Scanner(System.in);
      users = new ArrayList<User>();
      messages = new ArrayList<Message>();
      p = new PasswordEncryptionService();
      salt = p.generateSalt();
      usersFile = new File("users.txt");
      User admin = new Instructor("admin", p.getEncryptedPassword("secret", salt), salt);
      users.add(admin);
      users.addAll(readUserInfo());
      Message welcome = new Message(admin, "Welcome", 
         "Welcome to the discussion board!\n");
      messages.add(welcome);
   }

   /**
      Runs the simulation.
   */
   public void run()  throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
   {
      while (true)
      {
         if (currentUser == null)
         {
            login();
         }
         else
         {
            String userMenu = currentUser.getMenu(); 
            System.out.print("\n" + userMenu + "Command: ");
            String command = in.nextLine().toUpperCase();
            if (userMenu.contains(command + ")"))
            {
               if (command.equals("S")) { showMessages(); }
               else if (command.equals("N")) { newMessage(); }
               else if (command.equals("R")) { replyToMessage(); }
               else if (command.equals("L")) { logout(); }
               else if (command.equals("P")) { purgeMessage(); }
               else if (command.equals("A")) { addUser(); }
               else if (command.equals("E")) { editMessage(); }
               else if (command.equals("T")) { titleSearch(); }
            }
         }
      }
   }

   /**
      Finds a user with a given name.
      @param username the name to search
      @return the matching user, or null if none found
   */
   public User findUser(String username)
   {
      boolean found = false;
      for (int i = 0; !found && i < users.size(); i++)
      {
         User current = users.get(i);
         if (current.getName().equals(username)) { return current; }
      }
      return null;
   }
   
   /**
      Executes the login command.
   */
   public void login() throws NoSuchAlgorithmException, InvalidKeySpecException
   {
      System.out.print("User name: ");
      String username = in.nextLine();
      System.out.print("Password: ");
      String password = in.nextLine();
      User attemptedUsername = findUser(username);
      if(attemptedUsername == null){
         System.out.println("Username not found.");
      }
      else{
         if(p.authenticate(password, attemptedUsername.getUserEncryptedPassword(), attemptedUsername.getUserSalt())){
            currentUser = attemptedUsername;
         }
         else{
            System.out.println("Authentication failed.");
         }
      }
      
   }

   /**
      Executes the add user command.
   */
   public void addUser() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException
   {
      System.out.print("User name: ");
      String username = in.nextLine();
      System.out.print("Password: ");
      String password = in.nextLine();
      if (findUser(username) == null) 
      {         
         byte[] salt = p.generateSalt();
         byte[] encryptedPassword = p.getEncryptedPassword(password, salt);
         User user = new User(username, encryptedPassword, salt);
         users.add(user);
         writeUserInfo(user);
      }
      else
      {
         System.out.println("User already exists");
      }
   }

   /**
      Executes the logout command.
   */
   public void logout()
   {
      currentUser = null;
   }

   /**
      Shows all message titles and lets the user pick one for display.
   */
   public void showMessages()
   {
      for (int i = 0; i < messages.size(); i++)
      {
         Message msg = messages.get(i);
         if (msg == currentMessage) System.out.print("*");
         System.out.println((i + 1) + ") " + msg.getTitle());
      }
      System.out.print("Message number: ");
      int number = in.nextInt(); in.nextLine(); 
      currentMessage = messages.get(number - 1);
      currentMessage.display();
   }

   /**
      Executes the new message command.
   */
   public void newMessage()
   {
      System.out.print("Title: ");
      String title = in.nextLine();
      String text = "";
      System.out.println("Enter text, Q when done");
      boolean done = false;
      while (!done)
      {
         String line = in.nextLine();
         if (line.toUpperCase().equals("Q")) { done = true; }
         else { text = text + line + "\n"; }
      }
      currentMessage = new Message(currentUser, title, text);
      messages.add(currentMessage);
   }
   
   /**
      Executes the reply to message command.
   */
   public void replyToMessage()
   {
      String text = "";
      System.out.println("Enter reply, Q when done");
      boolean done = false;
      while (!done)
      {
         String line = in.nextLine();
         if (line.toUpperCase().equals("Q")) { done = true; }
         else { text = text + line + "\n"; }
      }
      currentMessage = new Response(currentUser, currentMessage, text);
      messages.add(currentMessage);
   }

   /**
      Executes the purge message command.
   */
   public void purgeMessage()
   {
      messages.remove(currentMessage);
   }

   /**
   Executes the edit message command.
   */
   public void editMessage(){
      for(int i = 0; i < messages.size(); i++){
         System.out.println((i + 1) + ") " + messages.get(i).getTitle());
      }
      System.out.print("Message number: ");
      int number = in.nextInt();
      currentMessage = messages.get(number - 1);

      String text = "";
      boolean done = false;
      while(!done){
         if(currentUser.getName().equals(currentMessage.getAuthor().getName())){
            String line = in.nextLine();
            if(line.toUpperCase().equals("Q")){
               done = true;
            }
            else{
               text = text + line + "\n";
            }
         }
         else{
            System.out.println("Error. Cannot edit another user's message.");
            done = true;
         }
         currentMessage.setText(text);
      }      
   }

   /**
      Searches message titles for search terms.
      @param search string terms to search for
      @return ArrayList of matching message titles
   */
   public void titleSearch(){
      System.out.print("Enter the search terms: ");
      String search = in.nextLine().toLowerCase();
      ArrayList<String> searchTerms = new ArrayList<String>(Arrays.asList(search.split(" ")));
      ArrayList<String> messageTitles;
      ArrayList<String> matchedTitles = new ArrayList<String>();

      for(int i = 0; i < messages.size(); i++){
         messageTitles = new ArrayList<String>(Arrays.asList(messages.get(i).getTitle().toLowerCase().split(" ")));
         for(int j = 0; j < searchTerms.size(); j++){
            if(messageTitles.contains(searchTerms.get(j))){
               matchedTitles.add(i+1 + ") " + messages.get(i).getTitle());
               break;
            }
         }
         messageTitles.clear();
      }
      for(int k = 0; k < matchedTitles.size(); k++){
         System.out.println("Matching titles:");
         System.out.println(matchedTitles.get(k));
      }
   }

   /**
      Writes user info to file on disk
   */
   public void writeUserInfo(User user) throws IOException {
      FileWriter fw = new FileWriter(usersFile, true);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(user.toString() + "\r\n");
      bw.close();
   }

   /**
      Reads user info from file on disk
      @return ArrayList<User> from user info
   */
   public ArrayList<User> readUserInfo() throws IOException {
      Scanner in = new Scanner(usersFile);
      String temp;
      String name;
      String pass;      
      String salt;
      ArrayList<User> user = new ArrayList<User>();

      while(in.hasNext()){
         temp = in.next();
         if(temp != ""){
            name = temp;
            pass = in.next();
            byte[] p = pass.getBytes();
            salt = in.next();
            byte[] s = salt.getBytes();
            user.add(new User(name, p, s));
         }
      }
      in.close();
      return user;
   }
}
