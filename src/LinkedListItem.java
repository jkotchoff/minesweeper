/**
 * Internet Explorer 5 would not recognise the Vector or Linked List
 * classes properly so I wrote my own.
 *
 * @author Jason Kotchoff
 */
class LinkedListItem {
   private Object obj = null;
   private LinkedListItem next;

   /**
    * Constructs a new LinkedListItem object.
    * @param obj the Object to be stored in this List node
    */
   public LinkedListItem(Object obj) {
      this.obj = obj;
      next = null;
   }

   /**
    * Returns the next Item in the list
    */
   public LinkedListItem getNext() {
      return next;
   }

   /**
    * Returns the Object stored in this LinkedListItem
    */
   public Object getObject() {
      return obj;
   }

   /**
    * Sets the Object stored in this LinkedListItem
    */
   public void setNext(LinkedListItem next) {
      this.next = next;
   }
}
