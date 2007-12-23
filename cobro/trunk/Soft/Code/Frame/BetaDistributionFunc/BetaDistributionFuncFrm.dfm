object frmBetaDistributionFunc: TfrmBetaDistributionFunc
  Left = 0
  Top = 0
  Width = 398
  Height = 81
  Constraints.MinHeight = 61
  TabOrder = 0
  object lblKMin: TLabel
    Left = 12
    Top = 10
    Width = 26
    Height = 13
    Caption = 'K min'
  end
  object lblKMax: TLabel
    Left = 204
    Top = 10
    Width = 29
    Height = 13
    Caption = 'K max'
  end
  object lblQ: TLabel
    Left = 224
    Top = 36
    Width = 8
    Height = 13
    Caption = 'Q'
  end
  object lblP: TLabel
    Left = 28
    Top = 36
    Width = 7
    Height = 13
    Caption = 'P'
  end
  object lblLSatiationl: TLabel
    Left = 6
    Top = 62
    Width = 32
    Height = 13
    Caption = 'Нас. л'
  end
  object lblRSatiationl: TLabel
    Left = 202
    Top = 62
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
  object edtKMax: TEdit
    Left = 240
    Top = 6
    Width = 121
    Height = 21
    Enabled = False
    TabOrder = 1
  end
  object edtP: TEdit
    Left = 46
    Top = 34
    Width = 111
    Height = 21
    TabOrder = 2
    Text = '0'
  end
  object UpDownP: TUpDown
    Left = 152
    Top = 33
    Width = 16
    Height = 22
    Min = 0
    Position = 50
    TabOrder = 3
    Wrap = False
    OnChangingEx = UpDownPChangingEx
  end
  object edtQ: TEdit
    Left = 240
    Top = 34
    Width = 111
    Height = 21
    TabOrder = 4
    Text = '0'
  end
  object UpDownQ: TUpDown
    Left = 351
    Top = 34
    Width = 16
    Height = 21
    Min = 0
    Position = 50
    TabOrder = 5
    Wrap = False
    OnChangingEx = UpDownQChangingEx
  end
  object edtLSatiation: TEdit
    Left = 46
    Top = 60
    Width = 111
    Height = 21
    Hint = 'Насыщение слева'
    ParentShowHint = False
    ShowHint = True
    TabOrder = 6
    Text = '0'
  end
  object UpDownLSatiation: TUpDown
    Left = 152
    Top = 59
    Width = 16
    Height = 22
    Min = 0
    Max = 10
    Position = 10
    TabOrder = 7
    Wrap = False
    OnChangingEx = UpDownLSatiationChangingEx
  end
  object edtRSatiation: TEdit
    Left = 240
    Top = 60
    Width = 111
    Height = 21
    Hint = 'Насыщение справа'
    ParentShowHint = False
    ShowHint = True
    TabOrder = 8
    Text = '0'
  end
  object UpDownUpDownRSatiation: TUpDown
    Left = 351
    Top = 60
    Width = 16
    Height = 21
    Min = 0
    Position = 10
    TabOrder = 9
    Wrap = False
    OnChangingEx = UpDownUpDownRSatiationChangingEx
  end
end
