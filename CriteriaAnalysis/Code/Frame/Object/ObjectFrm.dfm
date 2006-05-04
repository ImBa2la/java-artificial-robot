object frmObject: TfrmObject
  Left = 0
  Top = 0
  Width = 443
  Height = 277
  AutoSize = True
  TabOrder = 0
  Visible = False
  object dbgrdData: TDBGrid
    Left = 0
    Top = 29
    Width = 443
    Height = 248
    Align = alClient
    DataSource = dsData
    TabOrder = 0
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'MS Sans Serif'
    TitleFont.Style = []
  end
  object pnl: TPanel
    Left = 0
    Top = 0
    Width = 443
    Height = 29
    Align = alTop
    TabOrder = 1
    object lblObjectNameField: TLabel
      Left = 8
      Top = 8
      Width = 61
      Height = 13
      Caption = #1055#1086#1083#1077' '#1080#1084#1077#1085#1080
    end
    object cbxNameField: TComboBox
      Left = 76
      Top = 4
      Width = 145
      Height = 21
      Style = csDropDownList
      ItemHeight = 13
      TabOrder = 0
      OnChange = cbxNameFieldChange
    end
    object Button1: TButton
      Left = 242
      Top = 2
      Width = 103
      Height = 25
      Caption = 'Save AS CSV File'
      TabOrder = 1
      Visible = False
      OnClick = Button1Click
    end
  end
  object opndlgObject: TOpenDialog
    Filter = 'dBase Files (*.dbf)|*.dbf|Visual FoxPro Tables (*.dbf)|*.dbf'
    Left = 80
    Top = 98
  end
  object cnctnData: TADOConnection
    ConnectionString = 
      'Provider=MSDASQL.1;Persist Security Info=False;Data Source=Visua' +
      'l FoxPro Tables;Initial Catalog=c:'
    LoginPrompt = False
    Provider = 'MSDASQL.1'
    Left = 28
    Top = 56
  end
  object tblData: TADOTable
    Connection = cnctnData
    AfterOpen = tblDataAfterOpen
    AfterClose = tblDataAfterClose
    Left = 60
    Top = 56
  end
  object dsData: TDataSource
    DataSet = tblData
    Left = 92
    Top = 56
  end
  object qry: TADOQuery
    Connection = cnctnData
    CursorType = ctStatic
    Parameters = <>
    SQL.Strings = (
      'select distinct '#1053#1040#1047#1042#1040#1053#1048#1045'_'#1052' from dbase4')
    Left = 44
    Top = 104
  end
end
