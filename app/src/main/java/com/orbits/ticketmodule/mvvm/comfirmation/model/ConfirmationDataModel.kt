package com.orbits.ticketmodule.mvvm.comfirmation.model


data class ConfirmationDataModel(
    val transactionData: TransactionData,
    val status_message : String ?= null,
)

data class TransactionData(
    val isNewTransaction: Boolean,
    val receipts: List<Receipt>
)

data class Receipt(
    val action_code: String ?= null,
    val amount_authorized: AmountAuthorized ?= null,
    val amount_other: AmountOther ?= null,
    val application_cryptogram: String ?= null,
    val application_identifier: String ?= null,
    val approval_code: ApprovalCode ?= null,
    val card_expiration: String ?= null,
    val card_scheme: CardScheme ?= null,
    val card_scheme_sponsor: String ?= null,
    val cardholader_verfication_result: String ?= null,
    val created_at: String?= null,
    val cryptogram_information_data: String?= null,
    val currency: Currency?= null,
    val customer_reference_number: String?= null,
    val end_date: String?= null,
    val end_time: String?= null,
    val entry_mode: String?= null,
    val is_approved: Boolean?= false,
    val is_refunded: Boolean?= false,
    val is_reversed: Boolean?= false,
    val kernel_id: String?= null,
    val merchant: Merchant?= null,
    val pan: String?= null,
    val pan_suffix: String?= null,
    val payment_account_reference: String?= null,
    val pos_software_version_number: String?= null,
    val qr_code: String?= null,
    val receipt_id: String?= null,
    val receipt_line_one: ReceiptLineOne?= null,
    val receipt_line_two: ReceiptLineTwo?= null,
    val retrieval_reference_number: String?= null,
    val save_receipt_message: SaveReceiptMessage?= null,
    val start_date: String?= null,
    val start_time: String?= null,
    val status_message: StatusMessage?= null,
    val system_trace_audit_number: String?= null,
    val terminal_verification_result: String?= null,
    val thanks_message: ThanksMessage?= null,
    val tid: String?= null,
    val transaction_state_information: String?= null,
    val transaction_type: TransactionType?= null,
    val transaction_uuid: String?= null,
    val updated_at: String?= null,
    val verification_method: VerificationMethod?= null
)

data class AmountAuthorized(
    val label: Label,
    val value: String
)

data class AmountOther(
    val label: Label,
    val value: String
)

data class ApprovalCode(
    val label: Label,
    val value: String
)

data class CardScheme(
    val id: String,
    val name: Name
)

data class Currency(
    val arabic: String,
    val english: String
)

data class Merchant(
    val address: Address,
    val category_code: String,
    val id: String,
    val name: String
)

data class ReceiptLineOne(
    val arabic: String,
    val english: String
)

data class ReceiptLineTwo(
    val arabic: String,
    val english: String
)

data class SaveReceiptMessage(
    val arabic: String,
    val english: String
)

data class StatusMessage(
    val arabic: String,
    val english: String
)

data class ThanksMessage(
    val arabic: String,
    val english: String
)

data class TransactionType(
    val id: String,
    val name: String
)

data class VerificationMethod(
    val arabic: String,
    val english: String
)

data class Label(
    val arabic: String,
    val english: String
)

data class Name(
    val arabic: String,
    val english: String
)

data class Address(
    val arabic: String,
    val english: String
)
