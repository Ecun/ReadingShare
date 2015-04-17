package edu.bsu.cs222;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
	public abstract boolean hasAdd();
	public abstract void active();
	public boolean add(String name){
		return false;
	}
}
