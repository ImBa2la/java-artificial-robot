object frmSCCnvrsnPrmtr: TfrmSCCnvrsnPrmtr
  Left = 0
  Top = 0
  Width = 340
  Height = 75
  TabOrder = 0
  object lblMin: TLabel
    Left = 90
    Top = 4
    Width = 17
    Height = 13
    Anchors = [akTop, akRight]
    Caption = 'Min'
  end
  object lblSCFunction: TLabel
    Left = 12
    Top = 38
    Width = 97
    Height = 13
    Anchors = [akTop, akRight]
    Caption = 'Функция перевода'
  end
  object lblMax: TLabel
    Left = 224
    Top = 4
    Width = 20
    Height = 13
    Anchors = [akTop, akRight]
    Caption = 'Max'
  end
  object edtSMin: TEdit
    Left = 116
    Top = 0
    Width = 87
    Height = 21
    Anchors = [akTop, akRight]
    TabOrder = 0
  end
  object cmbxFunction: TComboBox
    Left = 115
    Top = 34
    Width = 225
    Height = 21
    Style = csDropDownList
    Anchors = [akTop, akRight]
    ItemHeight = 13
    TabOrder = 2
    Items.Strings = (
      'линейная'
      'плотность бета-распределения'
      'гауссовская')
  end
  object edtSMax: TEdit
    Left = 251
    Top = 0
    Width = 87
    Height = 21
    Anchors = [akTop, akRight]
    TabOrder = 1
  end
end
