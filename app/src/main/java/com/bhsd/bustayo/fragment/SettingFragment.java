package com.bhsd.bustayo.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.activity.InformationProvider;
import com.bhsd.bustayo.activity.LoginActivity;
import com.bhsd.bustayo.activity.PrivacyPolicy;

public class SettingFragment extends PreferenceFragment {

    ListPreference auto_refresh, set_sound;
    Preference account_info;
    Preference privacy_policy, information_provider, open_source;
    Preference question, complaint;
    boolean login_state;

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
        set_sound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return true;
            }
        });


        /* 개인정보 */
        account_info = findPreference("account_info");
        login_state = getAccountInfo();
        account_info.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String summary = preference.getSummary().toString();

                if(login_state) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("로그아웃");
                    builder.setMessage("로그아웃 하시겠습니까?");

                    builder.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences loginInfo = getActivity().getSharedPreferences("setting", 0);
                            SharedPreferences.Editor editor = loginInfo.edit();
                            editor.putString("id", "");
                            editor.putString("password", "");
                            editor.apply();

                            login_state = getAccountInfo();
                        }
                    });

                    builder.setNegativeButton("돌아가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //창 닫기
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivityForResult(intent,0);
                }

                return true;
            }
        });


        /* 약관 및 정책 */
        privacy_policy = findPreference("privacy_policy");
        privacy_policy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), PrivacyPolicy.class);
                startActivity(intent);
                return true;
            }
        });
        information_provider = findPreference("information_provider");
        information_provider.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), InformationProvider.class);
                startActivity(intent);
                return true;
            }
        });
        open_source = findPreference("open_source");
        open_source.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), preference.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });


        /* 고객지원 */
        question = findPreference("question");
        question.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), preference.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        complaint = findPreference("complaint");
        complaint.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), preference.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if(resultCode == getActivity().RESULT_OK){
                login_state = getAccountInfo();
            }
        }
    }

    /* 회원정보를 받아온다! */
    boolean getAccountInfo() {
        SharedPreferences loginInfo = getActivity().getSharedPreferences("setting", 0);
        String loginId = loginInfo.getString("id", "");
        if(loginId.equals("")) {
            account_info.setSummary("로그인 해주세요.");
            return false;
        } else {
            account_info.setSummary(loginId);
            return true;
        }
    }
}
