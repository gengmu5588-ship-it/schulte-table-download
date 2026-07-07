package com.schulte.table.ui.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.schulte.table.R;

public class GuideDetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_CONTENT = "content";

    public static GuideDetailFragment newInstance(String title, String content) {
        GuideDetailFragment fragment = new GuideDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args == null) return;

        String title = args.getString(ARG_TITLE, "");
        String content = args.getString(ARG_CONTENT, "");

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar_guide_detail);
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        });

        TextView tvContent = view.findViewById(R.id.tv_guide_content);
        tvContent.setText(content);
    }
}
