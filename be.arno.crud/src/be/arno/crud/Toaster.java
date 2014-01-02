package be.arno.crud;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Toaster {
	
	public static final int ERROR   = 0;
	public static final int SUCCESS = 1;
	
	public static void showToast(Context context, int kind, int message) {
				
		LayoutInflater inflater =
				(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout;
		switch (kind) {
		case ERROR:
			layout = inflater.inflate(R.layout.toast_error, null);
			break;
		case SUCCESS:
			layout = inflater.inflate(R.layout.toast_success, null);
			break;
		default:
			layout = inflater.inflate(R.layout.toast_default, null);
			break;
		}
	    
	    TextView txvw = (TextView) layout.findViewById(R.id.toast_error_txvw);
	    txvw.setText(message);
	    Toast toast = new Toast(context);
	    toast.setGravity(Gravity.CENTER, 0, 0);
	    toast.setDuration(Toast.LENGTH_LONG);
	    toast.setView(layout);
	    toast.show();
	}
}
