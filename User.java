/**
   A user of the discussion board.
*/
public class User
{
   private String name;
   private String password;

   /**
      Constructs a user.
      @param aName the user's name
      @param aPassword the user's password
   */
   public User(String aName, String aPassword)
   {
      name = aName;
      password = aPassword;
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
      Checks whether the password matches.
      @param aPassword the password to match
      @return true if the user's password matches aPassword
   */
   public boolean authenticate(String aPassword)
   {
      return password.equals(aPassword);
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
      + "N)ew message\n"
      + "L)ogout\n";
   }
}
