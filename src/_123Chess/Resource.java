package _123Chess;


import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 *
 * @author kaitlyn.yang
 */
public class Resource {
    protected static ResourceBundle resources;
    static{
        try{
            resources = ResourceBundle.getBundle("res.Resources",Locale.getDefault());
        }catch(Exception e){
            System.out.println("Resources not found");
            javax.swing.JOptionPane.showMessageDialog(null,
                    "123Chess properties not found",
                    "Error",javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    public String getResourceString(String key){
        String str;
        try{
            str = resources.getString(key);
        }catch(Exception e){
            str = null;
        }
        return str;
    }
}
