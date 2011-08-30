/**
 * Custom Linked List Data Structure
 *
 * @author Jason Kotchoff
 */
class JKLinkedList {

   private LinkedListItem head;

   /**
    * Constructs a new JKLinkedList object.
    */
   public JKLinkedList() {
      head = null;
   }

   /**
    * Adds an object to the end of the Linked List
    */
   public void add(Object obj) {
      if (head == null) {
         head = new LinkedListItem(obj);
      }
      else {
         LinkedListItem iterator = head;

         while (iterator.getNext() != null)
            iterator = iterator.getNext();

         iterator.setNext(new LinkedListItem(obj));
      }
   }

   /**
    * Returns the first Object in the list (the head)
    */
   public Object getFirst() {
      return head.getObject();
   }

   /**
    * Returns the head of the list
    */
   public LinkedListItem getHead() {
      return head;
   }

   /**
    * Returns true if the linked list is empty
    */
   public boolean isEmpty() {
      return (head == null);
   }

   /**
    * Returns the Object from the List
    */
   public void remove(Object obj) {
      LinkedListItem iterator = head;

      if (iterator.getObject() == obj) {
         head = head.getNext();
         return;
      }

      while (iterator.getNext().getObject() != obj && iterator.getNext() != null)
         iterator = iterator.getNext();

      if (iterator.getNext().getObject() == obj) {
         iterator.setNext((iterator.getNext()).getNext());
      }
      else
         System.err.println("Object not found to be deleted from Linked List");
   }
}
