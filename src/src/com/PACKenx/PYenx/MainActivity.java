package com.PACKenx.PYenx;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;

import java.lang.Process;

public class MainActivity extends Activity {

	TextView show = null;
	String infonow = "--log--\n";
	String toas = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		show = (TextView)this.findViewById(R.id.showenx);
		String runfile = getApplicationContext().getFilesDir().getAbsolutePath();
		String know = this.getString(R.string.know);
		String topbe = this.getString(R.string.topbe);
		String topaf = this.getString(R.string.topaf);
		String afbe = this.getString(R.string.afbe);
		String afaf = this.getString(R.string.afaf);
		String scr = topbe+runfile+topaf;
		String rumode = afbe+runfile+afaf;
		show.setText(know+scr+rumode);
		Button inst = (Button)this.findViewById(R.id.inst);
		inst.setText(this.getString(R.string.inst));
	}

	public void runenx(View view) {
		toas = this.getString(R.string.toas);
		String fls = getApplicationContext().getFilesDir().getAbsolutePath();
		String path = fls.substring(0,fls.lastIndexOf("/"));
		String unzip = path+"/lib/libunzip.so -o "+path+"/lib/libpypack.so -d "+fls;
		shell(unzip);
		String read = "toolbox chmod -R 0775 "+fls+"/pydir";
		shell(read);
		String rwtmp = "toolbox chmod -R 0777 "+fls+"/pydir/tmp";
		shell(rwtmp);
		//show.setText(infonow);
		infonow = "";
		Toast.makeText(getApplicationContext(),toas,Toast.LENGTH_SHORT).show();
	}

	public void shell(String cmd) {
		Process p =null;
		try {
			p = Runtime.getRuntime().exec(cmd);
			String[] op =runifo(p);
			p.waitFor();
			Thread.sleep(1000);
			p.destroy();
			infonow += op[0] + op[1] + "------\n";
		} catch (IOException e) {} catch (InterruptedException e) {toas = this.getString(R.string.toaserr);}
	}

	public String[] runifo (Process p) {
		final String[] s = new String[2];
		final InputStream intmp = p.getInputStream();
		final InputStream errtmp = p.getErrorStream();
		new Thread() { public void run() {
				s[0] = "info:\n";
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(intmp));
					String line = null;
					while ((line = in.readLine()) != null)
					{
						s[0] += line + "\n";
					}
				} catch (IOException e) {}
				finally {
					try {
						intmp.close();
					} catch (IOException e) {}
				}
			}}.start();
		new Thread() { public void run() {
				s[1] = "err:\n";
				try {
					BufferedReader err = new BufferedReader(new InputStreamReader(errtmp));
					String error = null;
					while ((error = err.readLine()) != null)
					{
						s[1] += error + "\n";
					}
				} catch (IOException e) {}
				finally {
					try {
						errtmp.close();
					} catch (IOException e) {}
				}
			}}.start();
		return s;
	}

}
