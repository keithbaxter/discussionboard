/**
   A discussion board message.
*/
import java.util.Date;

public class Message
{
   private User author;
   private String title;
   private String text;
   private Date date = new Date();

   /**
      Constructs a message.
      @param author the message author
      @param title the message title
      @param text the message text
   */
   public Message(User author, String title, String text)
   {
      this.author = author;
      this.title = title;
      this.text = text;
   }

   /**
      Gets the message title.
      @return the title
   */
   public String getTitle()
   {
      return title;
   }

   /**
      Displays the message.
   */
   public void display()
   {
      System.out.println("From: " + author.getName());
      System.out.println("Title: " + getTitle());
      System.out.println("Date: " + date); 
      System.out.print(text);
   }

   /**
   Edits the message text.
   */
   public void setText(String text){
      this.text = text + "\n" + "Edited by " + author.getName() + " at " + date + ".\n";
   }

   /**
   Gets the author of the message.
   @return the author
   */
   public User getAuthor(){
      return author;
   }
}
