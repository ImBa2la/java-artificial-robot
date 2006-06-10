object frmExponentialFunc: TfrmExponentialFunc
  Left = 0
  Top = 0
  Width = 370
  Height = 92
  TabOrder = 0
  object lblKMin: TLabel
    Left = 12
    Top = 10
    Width = 26
    Height = 13
    Caption = 'K min'
  end
  object lblBase: TLabel
    Left = 178
    Top = 36
    Width = 56
    Height = 13
    Caption = 'Основание'
  end
  object lblKMax: TLabel
    Left = 204
    Top = 10
    Width = 29
    Height = 13
    Caption = 'K max'
  end
  object lblLSatiationl: TLabel
    Left = 6
    Top = 60
    Width = 32
    Height = 13
    Caption = 'Нас. л'
  end
  object lblRSatiationl: TLabel
    Left = 202
    Top = 60
    Width = 32
    Height = 13
    Caption = 'Нас. п'
  end
  object edtKMin: TEdit
    Left = 46
    Top = 6
    Width = 121
    Height = 21
    Enabled = False
    TabOrder = 0
  end
  object edtBase: TEdit
    Left = 240
    Top = 32
    Width = 111
    Height = 21
    TabOrder = 1
    Text = '0'
  end
  object UpDownP: TUpDown
    Left = 346
    Top = 31
    Width = 16
    Height = 22
    Min = 0
    Position = 10
    TabOrder = 2
    Wrap = False
    OnChangingEx = UpDownPChangingEx
  end
  object edtKMax: TEdit
    Left = 240
    Top = 6
    Width = 121
    Height = 21
    Enabled = False
    TabOrder = 3
  end
  object edtLSatiation: TEdit
    Left = 44
    Top = 56
    Width = 111
    Height = 21
    Hint = 'Насыщение слева'
    ParentShowHint = False
    ShowHint = True
    TabOrder = 4
    Text = '0'
  end
  object UpDownLSatiation: TUpDown
    Left = 152
    Top = 55
    Width = 16
    Height = 22
    Min = 0
    Position = 50
    TabOrder = 5
    Wrap = False
    OnChangingEx = UpDownLSatiationChangingEx
  end
  object edtRSatiation: TEdit
    Left = 240
    Top = 58
    Width = 111
    Height = 21
    Hint = 'Насыщение справа'
    ParentShowHint = False
    ShowHint = True
    TabOrder = 6
    Text = '0'
  end
  object UpDownUpDownRSatiation: TUpDown
    Left = 346
    Top = 58
    Width = 16
    Height = 21
    Min = 0
    Position = 50
    TabOrder = 7
    Wrap = False
    OnChangingEx = UpDownRSatiationChangingEx
  end
end
