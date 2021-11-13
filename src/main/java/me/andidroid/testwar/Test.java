package me.andidroid.testwar;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "test", schema = "public")
@EntityListeners(MessagingEntityListener.class)
public class Test
{
    
    @Id
    private long id;
    @NotNull
    private String text;
    
    public Test()
    {
    }
    
    public Test(long id, String text)
    {
        this.id = id;
        this.text = text;
    }
    
    /**
     * @return the id
     */
    public long getId()
    {
        return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(long id)
    {
        this.id = id;
    }
    
    /**
     * @return the text
     */
    public String getText()
    {
        return text;
    }
    
    /**
     * @param text the text to set
     */
    public void setText(String text)
    {
        this.text = text;
    }
    
    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return super.toString();
    }
    
}
