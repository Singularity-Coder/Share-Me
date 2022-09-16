package com.singularitycoder.shareme

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.core.widget.doAfterTextChanged
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputLayout
import com.singularitycoder.shareme.databinding.FragmentPersonDetailBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonDetailBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance(
            adapterPosition: Int,
            person: Person
        ) = PersonDetailBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM_ADAPTER_POSITION, adapterPosition)
                putParcelable(ARG_PARAM_PERSON, person)
            }
        }
    }

    private lateinit var binding: FragmentPersonDetailBottomSheetBinding

    private var adapterPosition: Int = 0
    private var person: Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterPosition = arguments?.getInt(ARG_PARAM_ADAPTER_POSITION) ?: 0
        person = arguments?.getParcelable<Person>(ARG_PARAM_PERSON)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPersonDetailBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTransparentBackground()
        binding.setupUI()
        binding.setupUserActionListeners()
        binding.observeForData()
    }

    // https://stackoverflow.com/questions/40616833/bottomsheetdialogfragment-listen-to-dismissed-by-user-event
    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
//        viewModel.contactAccidentBackupLiveData.value = Contact(
//            name = binding.etName.editText?.text.toString(),
//            mobileNumber = binding.etPhoneNumber.editText?.text.toString(),
//            dateAdded = timeNow,
//            imagePath = "",
//            videoPath = ""
//        )
    }

    private fun FragmentPersonDetailBottomSheetBinding.setupUI() {
        ivImageExpanded.layoutParams.height = deviceWidth()
        ivImage.load(person?.tempImageDrawable) {
            placeholder(R.drawable.ic_placeholder_big)
        }
        ivImageExpanded.load(person?.tempImageDrawable) {
            placeholder(R.drawable.ic_placeholder_big)
        }
        tvPersonName.text = person?.name
        tvTimeYouCanAfford.text = "You can afford ${person?.iCanAfford} of ${person?.name}'s time."
        tvProfession.text = person?.profession
        tvHourlyWorthCount.text = "${person?.hourlyWorth} USD"
        person?.skillsList?.forEach { it: String ->
            val chip = Chip(requireContext()).apply {
                text = it
                isCheckable = false
                isClickable = false
                chipBackgroundColor = ColorStateList.valueOf(requireContext().color(R.color.black_50))
//                setTextColor(nnContext.color(R.color.purple_500))
                elevation = 0f
                setOnClickListener {
                }
            }
            chipGroupSkills.addView(chip)
        }
//        val itemBinding: ItemSmsBinding = ItemSmsBinding.inflate(LayoutInflater.from(requireContext()), binding.llServices, false)
//        itemBinding.tvNumber.text = sms.number
        /**
         * https://stackoverflow.com/questions/3429546/how-do-i-add-a-bullet-symbol-in-textview
         * • = \u2022,   ● = \u25CF,   ○ = \u25CB,   ▪ = \u25AA,   ■ = \u25A0,   □ = \u25A1,   ► = \u25BA
         * */
        person?.servicesList?.forEach { it: String ->
            val serviceTextView = TextView(context).apply {
                text = "\u25CF  $it"
                setTextColor(requireContext().color(R.color.title_color))
                setMargins(start = 0, top = 8.dpToPx(), end = 0, bottom = 0)
                setPadding(0, 8.dpToPx(), 0, 0)
            }
            binding.llServices.addView(serviceTextView)
        }
    }

    private fun FragmentPersonDetailBottomSheetBinding.setupUserActionListeners() {
        ivImage.setOnClickListener {
            ivImageExpanded.isVisible = true
            ivImage.isVisible = false
        }
        ivImageExpanded.setOnClickListener {
            ivImage.isVisible = true
            ivImageExpanded.isVisible = false
        }

        etName.doTextFieldEmptyValidation()
        etName.setBoxStrokeOnFocusChange()

        etPhoneNumber.doTextFieldEmptyValidation()
        etPhoneNumber.setBoxStrokeOnFocusChange()
    }

    private fun FragmentPersonDetailBottomSheetBinding.observeForData() {

    }

    private fun FragmentPersonDetailBottomSheetBinding.isValidateInput(): Boolean {
        if (etName.editText?.text.isNullOrBlank()) {
            etName.boxStrokeWidth = 2.dpToPx()
            etName.error = "This is required!"
            return false
        }

        if (etPhoneNumber.editText?.text.isNullOrBlank()) {
            etPhoneNumber.boxStrokeWidth = 2.dpToPx()
            etPhoneNumber.error = "This is required!"
            return false
        }

        return true
    }

    private fun TextInputLayout.doTextFieldEmptyValidation() {
        editText?.doAfterTextChanged { it: Editable? ->
            if (editText?.text.isNullOrBlank()) {
                error = "This is required!"
            } else {
                error = null
                isErrorEnabled = false
            }
        }
    }

    private fun TextInputLayout.setBoxStrokeOnFocusChange() {
        editText?.setOnFocusChangeListener { view, isFocused ->
            boxStrokeWidth = if (isFocused) 2.dpToPx() else 0
        }
    }
}

private const val ARG_PARAM_ADAPTER_POSITION = "ARG_PARAM_ADAPTER_POSITION"
private const val ARG_PARAM_PERSON = "ARG_PARAM_PERSON"