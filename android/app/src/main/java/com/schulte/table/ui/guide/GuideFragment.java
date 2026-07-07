package com.schulte.table.ui.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.schulte.table.R;
import com.schulte.table.data.model.GuideItem;

import java.util.ArrayList;
import java.util.List;

public class GuideFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvGuide = view.findViewById(R.id.rv_guide);
        rvGuide.setLayoutManager(new LinearLayoutManager(getContext()));

        List<GuideItem> items = buildGuideItems();
        GuideAdapter adapter = new GuideAdapter(items, item -> {
            GuideDetailFragment detail = GuideDetailFragment.newInstance(item.title, item.content);
            getParentFragmentManager().beginTransaction()
                    .replace(((ViewGroup) requireView().getParent()).getId(), detail)
                    .addToBackStack(null)
                    .commit();
        });
        rvGuide.setAdapter(adapter);
    }

    private List<GuideItem> buildGuideItems() {
        List<GuideItem> items = new ArrayList<>();
        items.add(new GuideItem(0, getString(R.string.guide_title_1), getString(R.string.guide_summary_1), getString(R.string.guide_content_1)));
        items.add(new GuideItem(0, getString(R.string.guide_title_2), getString(R.string.guide_summary_2), getString(R.string.guide_content_2)));
        items.add(new GuideItem(0, getString(R.string.guide_title_3), getString(R.string.guide_summary_3), getString(R.string.guide_content_3)));
        items.add(new GuideItem(0, getString(R.string.guide_title_4), getString(R.string.guide_summary_4), getString(R.string.guide_content_4)));
        items.add(new GuideItem(0, getString(R.string.guide_title_5), getString(R.string.guide_summary_5), getString(R.string.guide_content_5)));
        items.add(new GuideItem(0, getString(R.string.guide_title_6), getString(R.string.guide_summary_6), getString(R.string.guide_content_6)));
        items.add(new GuideItem(0, getString(R.string.guide_title_7), getString(R.string.guide_summary_7), getString(R.string.guide_content_7)));
        items.add(new GuideItem(0, getString(R.string.guide_title_8), getString(R.string.guide_summary_8), getString(R.string.guide_content_8)));
        return items;
    }
}
