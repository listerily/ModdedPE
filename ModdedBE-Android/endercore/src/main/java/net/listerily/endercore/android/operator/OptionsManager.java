package net.listerily.endercore.android.operator;

import com.google.gson.Gson;

import net.listerily.endercore.android.EnderCore;
import net.listerily.endercore.android.interf.IOptionsData;

import java.io.IOException;

public class OptionsManager {
    private OptionsJsonBean optionsJsonBean;
    private final IOptionsData optionsData;

    public OptionsManager(EnderCore core) throws IOException {
        this.optionsData = core.getOptionsData();
        this.optionsJsonBean = new Gson().fromJson(optionsData.getJSONAsString(new Gson().toJson(new OptionsJsonBean())), OptionsJsonBean.class);
    }

    public void setUseNMods(boolean val) {
        optionsJsonBean.use_nmods = val;
    }
    public boolean getUseNMods() {
        return optionsJsonBean.use_nmods;
    }

    public void setAutoLicense(boolean val) {
        optionsJsonBean.auto_license = val;
    }
    public boolean getAutoLicense() {
        return optionsJsonBean.auto_license;
    }

    public void setRedirectGameDir(boolean val) {
        optionsJsonBean.redirect_game_dir = val;
    }
    public boolean getRedirectGameDir() {
        return optionsJsonBean.redirect_game_dir;
    }

    public NModOptionsElement[] getInstalledNMods(){
        return optionsJsonBean.installed_nmods;
    }
    public void removeNModElement(String uuid){
        NModOptionsElement[] newArray = new NModOptionsElement[optionsJsonBean.installed_nmods.length - 1];
        for(int i = 0,j = 0;i < optionsJsonBean.installed_nmods.length;++i){
            NModOptionsElement element = optionsJsonBean.installed_nmods[i];
            if(!element.uuid.equals(uuid))
                newArray[j++] = element;
        }
        optionsJsonBean.installed_nmods = newArray;
    }
    public void setNModElementEnabled(String uuid, boolean enabled){
        for(int i = 0;i < optionsJsonBean.installed_nmods.length;++i)
            if (optionsJsonBean.installed_nmods[i].uuid.equals(uuid))
                optionsJsonBean.installed_nmods[i].enabled = enabled;
    }
    public boolean isNModEnabled(String uuid) {
        for(int i = 0;i < optionsJsonBean.installed_nmods.length;++i)
            if (optionsJsonBean.installed_nmods[i].uuid.equals(uuid))
                return optionsJsonBean.installed_nmods[i].enabled;
        return false;
    }
    public boolean isNModExists(String uuid){
        for(int i = 0;i < optionsJsonBean.installed_nmods.length;++i)
            if (optionsJsonBean.installed_nmods[i].uuid.equals(uuid))
                return true;
        return false;
    }
    public void addNewNModElement(String uuid, boolean enabled){
        NModOptionsElement[] newArray = new NModOptionsElement[optionsJsonBean.installed_nmods.length + 1];
        System.arraycopy(optionsJsonBean.installed_nmods, 0, newArray, 0, optionsJsonBean.installed_nmods.length);
        newArray[optionsJsonBean.installed_nmods.length] = new NModOptionsElement();
        newArray[optionsJsonBean.installed_nmods.length].uuid = uuid;
        newArray[optionsJsonBean.installed_nmods.length].enabled = true;
        optionsJsonBean.installed_nmods = newArray;
    }

    public void saveDataToFile() throws IOException{
        optionsData.setJSONAsString(new Gson().toJson(optionsJsonBean));
    }

    public void reset(){
        optionsJsonBean = new OptionsJsonBean();
    }

    public final static class NModOptionsElement
    {
        public String uuid = null;
        public boolean enabled = true;
    }

    private final static class OptionsJsonBean
    {
        private boolean use_nmods = false;
        private boolean auto_license = true;
        private boolean redirect_game_dir = true;
        private NModOptionsElement[] installed_nmods = new NModOptionsElement[0];
    }
}