package com.bhsd.bustayo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.activity.LoginActivity;

public class SettingFragment extends PreferenceFragment {

    ListPreference auto_refresh, set_sound;
    Preference account_info;
    Preference service, location, user_info, information, use_open_source;
    Preference question, complaint;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        /* 기본설정 */
        auto_refresh = (ListPreference) findPreference("auto_refresh");
        auto_refresh.setSummary(auto_refresh.getValue() + "초"); // 설정된 값 불러오기
        // 값이 변경되면 summary 변경
        auto_refresh.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary("" + newValue + "초");
                return true;
            }
        });
        set_sound = (ListPreference) findPreference("set_sound");


        /* 개인정보 */
        account_info = findPreference("account_info");
        account_info.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String summary = preference.getSummary().toString();

                if(summary.equals("로그인 해주세요.")) {
                     Intent intent = new Intent(getContext(), LoginActivity.class);
                     startActivity(intent);
                } else {
                    // 로그인 상태일때 처리
                }

                return true;
            }
        });


        /* 약관 및 정책 */
        service = findPreference("service");
        service.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), service.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        location = findPreference("location");
        location.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), location.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        user_info = findPreference("user_info");
        user_info.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), user_info.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        information = findPreference("information");
        information.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), information.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        use_open_source = findPreference("use_open_source");
        use_open_source.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), use_open_source.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });


        /* 고객지원 */
        question = findPreference("question");
        question.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), question.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        complaint = findPreference("complaint");
        complaint.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), complaint.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });


    }
}
