package model;

/**
 * Represents a contact with attributes such as contact ID, contact name, and email.
 */
public class Contact {
    // Attributes
    public int contactId;
    public String contactName;
    public String email;

    /**
     * Constructs a Contact object with specified details.
     *
     * @param contactId    The unique identifier for the contact.
     * @param contactName  The name of the contact.
     * @param email        The email address of the contact.
     */
    public Contact(int contactId, String contactName, String email) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     * Gets the contact ID.
     *
     * @return The contact ID.
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets the contact ID.
     *
     * @param contactId The new contact ID to set.
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Gets the contact name.
     *
     * @return The contact name.
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets the contact name.
     *
     * @param contactName The new contact name to set.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Gets the email address of the contact.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the contact.
     *
     * @param email The new email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Generates a string representation of the Contact object.
     *
     * @return A formatted string containing relevant information about the contact.
     */
    @Override
    public String toString() {
        return ("Contact {" + Integer.toString(contactId) + " contactName = " + contactName + " }");
    }
}
