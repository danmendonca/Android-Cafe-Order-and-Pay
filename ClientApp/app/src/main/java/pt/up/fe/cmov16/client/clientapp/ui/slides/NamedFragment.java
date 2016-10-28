package pt.up.fe.cmov16.client.clientapp.ui.slides;

import android.support.v4.app.Fragment;

public class NamedFragment extends Fragment {
    protected String tittle;

    @Override
    public String toString(){
        return (tittle!=null)? tittle : "NoTittleDefined";
    }

    public void focusObtained(){}
}
