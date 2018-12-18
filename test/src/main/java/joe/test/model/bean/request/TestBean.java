package joe.test.model.bean.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * @author : Joe
 * @version V1.0


 *  测试用bean
 * Date Date : 2018年09月03日 13:48
 */
public class TestBean {
    @Length(min = 1, max = 5)
    @NotBlank
    private String string;
    @Min(1)
    private int aInt;
    private boolean aBool;
    private long aLong;
    private Date date;
    @NotEmpty
    private List<String> strings;

    public String getString() {
        return string;
    }

    public TestBean setString(String string) {
        this.string = string;
        return this;
    }

    public int getaInt() {
        return aInt;
    }

    public TestBean setaInt(int aInt) {
        this.aInt = aInt;
        return this;
    }

    public boolean isaBool() {
        return aBool;
    }

    public TestBean setaBool(boolean aBool) {
        this.aBool = aBool;
        return this;
    }

    public long getaLong() {
        return aLong;
    }

    public TestBean setaLong(long aLong) {
        this.aLong = aLong;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public TestBean setDate(Date date) {
        this.date = date;
        return this;
    }

    public List<String> getStrings() {
        return strings;
    }

    public TestBean setStrings(List<String> strings) {
        this.strings = strings;
        return this;
    }
}
