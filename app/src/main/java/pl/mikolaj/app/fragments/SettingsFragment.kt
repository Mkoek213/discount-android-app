package pl.mikolaj.app.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import pl.mikolaj.app.LanguageChangeListener
import pl.revolshen.app.R
import pl.revolshen.app.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var languageChangeListener: LanguageChangeListener? = null
    private var _binding: FragmentSettingsBinding? = null


    private val languageOptions = listOf("English", "Polski")

    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var languageButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var switchMode: SwitchCompat = view.findViewById(R.id.switchMode)
        var nightMode: Boolean
        sharedPreferences = requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE)

        nightMode = sharedPreferences.getBoolean("nightMode", false)
        if(nightMode) {
            switchMode.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        switchMode.setOnClickListener{view ->
            if(nightMode){
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                val editor = sharedPreferences.edit()
                editor.putBoolean("nightMode", false)
                editor.apply()
        }else{
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                val editor = sharedPreferences.edit()
                editor.putBoolean("nightMode", true)
                editor.apply()
            }

        }
        // Initialize languageButton using the binding object
        languageButton = binding.languageButton
        binding.languageButton.setOnClickListener {
            val currentLanguage = sharedPreferences.getString("language", "English")
            val currentLanguageIndex = languageOptions.indexOf(currentLanguage)
            val languageDialog = AlertDialog.Builder(requireContext())
                .setTitle("Select Language")
                .setSingleChoiceItems(languageOptions.toTypedArray(), currentLanguageIndex) { dialog, which ->
                    val selectedLanguage = languageOptions[which]
                    sharedPreferences.edit().putString("language", selectedLanguage).apply()
                    languageChangeListener?.onLanguageChanged(selectedLanguage)
                    dialog.dismiss()
                }
                .create()
            languageDialog.show()
        }


    }
    fun setLanguageChangeListener(listener: LanguageChangeListener) {
        languageChangeListener = listener
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


//switchMode = findViewById(R.id.switchMode);
//sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
//nightMode = sharedPreferences.getBoolean("nightMode", false);
//
//if(nightMode) {
//    switchMode.setChecked(true);
//    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//}
//switchMode.setOnClickListener(new View.OnClickListener(){
//    @Override
//    public void onClick(View view) {
//        if(nightMode){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            editor = sharedPreferences.edit();
//            editor.putBoolean("nightMode", false);
//
//        }else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            editor = sharedPreferences.edit();
//            editor.putBoolean("nightMode", true);
//        }
//        editor.apply()
//    }
//})

//SwitchCompat switchMode;
//boolean nightMode;
//SharedPreferences sharedPreferences;
//SharedPreferences.Editor editor;


