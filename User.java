/**
   A user of the discussion board.
*/
public class User
{
   private String name;
   private byte[] password;
   private byte[] salt;

   /**
      Constructs a user.
      @param name the user's name
      @param password the user's password
   */
   public User(String name, byte[] password, byte[] salt)
   {
      this.name = name;
      this.password = password;
      this.salt = salt;
   }

   /**
      Gets the user's name.
      @return the name
   */
   public String getName()
   {
      return name;
   }

   /**
      Gets user's encrypted password.
      @return the user's encrypted password
   */
    public byte[] getUserEncryptedPassword(){
        return password;
   }

   /**
      Gets the user's salt.
      @return the user's salt
   */
    public byte[] getUserSalt(){
        return salt;
   }

   /**
        @return the paramaters as a string   
   */
   public String toString(){
        String p = new String(password);
        String s = new String(salt);        
        return name + "\r\n" + p + "\r\n" + s;
   }


   /**
      Gets the menu for the user.
      @return the menu
   */
   public String getMenu()
   {
      return "S)how messages\n"
      + "R)eply to message\n"
      + "E)dit message\n"
      + "T)itle search\n"
      + "N)ew message\n"
      + "L)ogout\n";
   }
}
