object frmGaussianFunc: TfrmGaussianFunc
  Left = 0
  Top = 0
  Width = 372
  Height = 66
  TabOrder = 0
  object lblKMin: TLabel
    Left = 12
    Top = 10
    Width = 26
    Height = 13
    Caption = 'K min'
  end
  object lblLSatiationl: TLabel
    Left = 8
    Top = 36
    Width = 32
    Height = 13
    Caption = 'Нас. л'
  end
  object lblRSatiationl: TLabel
    Left = 202
    Top = 36
    Width = 32
    Height = 13
    Caption = 'Нас. п'
  end
  object lblKMax: TLabel
    Left = 204
    Top = 10
    Width = 29
    Height = 13
    Caption = 'K max'
  end
  object edtKMin: TEdit
    Left = 46
    Top = 6
    Width = 121
    Height = 21
    Enabled = False
    TabOrder = 0
  end
  object edtLSatiation: TEdit
    Left = 46
    Top = 32
    Width = 111
    Height = 21
    Hint = 'Насыщение слева'
    ParentShowHint = False
    ShowHint = True
    TabOrder = 1
    Text = '0'
  end
  object UpDownLSatiation: TUpDown
    Left = 152
    Top = 31
    Width = 16
    Height = 22
    Min = 0
    Position = 50
    TabOrder = 2
    Wrap = False
    OnChangingEx = UpDownLSatiationChangingEx
  end
  object edtRSatiation: TEdit
    Left = 240
    Top = 32
    Width = 111
    Height = 21
    Hint = 'Насыщение справа'
    ParentShowHint = False
    ShowHint = True
    TabOrder = 3
    Text = '0'
  end
  object UpDownUpDownRSatiation: TUpDown
    Left = 350
    Top = 32
    Width = 16
    Height = 21
    Min = 0
    Position = 50
    TabOrder = 4
    Wrap = False
    OnChangingEx = UpDownRSatiationChangingEx
  end
  object edtKMax: TEdit
    Left = 240
    Top = 6
    Width = 127
    Height = 21
    Enabled = False
    TabOrder = 5
  end
end
