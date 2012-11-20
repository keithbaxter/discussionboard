/**
   A response to a discussion board message.
*/
public class Response extends Message
{
   private Message original;

   /**
      Constructs a response.
      @param author the message author
      @param original the message to which this is a response
      @param text the message text
   */
   public Response(User author, Message original, String text)
   {
      super(author, "", text);
      this.original = original;
   }

   public String getTitle()
   {
      return "Re: " + original.getTitle();
   }

   public void display()
   {
      super.display();
      System.out.println("---");
      original.display();
   }
}
