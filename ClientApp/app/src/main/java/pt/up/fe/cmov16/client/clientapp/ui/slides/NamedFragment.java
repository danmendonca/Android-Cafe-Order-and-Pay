package pt.up.fe.cmov16.client.clientapp.ui.slides;

import android.support.v4.app.Fragment;

public class NamedFragment extends Fragment {
    protected String title;

    @Override
    public String toString() {
        return (title != null) ? title : "NoTittleDefined";
    }

}
