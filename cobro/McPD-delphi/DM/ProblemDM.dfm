object dmProblem: TdmProblem
  OldCreateOrder = False
  OnDestroy = DataModuleDestroy
  Left = 314
  Top = 220
  Height = 351
  Width = 568
  object opndlg: TOpenDialog
    Filter = #1052#1050#1047' (*.mcp)|*.mcp'
    Left = 68
    Top = 52
  end
  object svdlg: TSaveDialog
    DefaultExt = 'mcp'
    Filter = #1052#1050#1047' (*.mcp)|*.mcp'
    Options = [ofOverwritePrompt, ofHideReadOnly, ofEnableSizing]
    Left = 70
    Top = 10
  end
  object cnctnSaveLoad: TADOConnection
    ConnectionString = 
      'Provider=MSDASQL.1;Persist Security Info=False;Extended Properti' +
      'es="DSN=dBASE Files;DBQ=E:\DIPLOM\TREE\DATA;DefaultDir=E:\DIPLOM' +
      '\TREE\DATA;DriverId=533;FIL=dBase 5.0;MaxBufferSize=2048;PageTim' +
      'eout=5;";Initial Catalog=E:\DIPLOM\TREE\DATA'
    LoginPrompt = False
    Provider = 'MSDASQL.1'
    Left = 138
    Top = 16
  end
  object cmd: TADOCommand
    CommandText = 'CREATE TABLE node ( m_Name CHAR(20))'
    Connection = cnctnSaveLoad
    Parameters = <>
    Left = 136
    Top = 66
  end
  object tbl: TADOTable
    Connection = cnctnSaveLoad
    Left = 180
    Top = 68
  end
  object tblCR: TADOTable
    Connection = cnctnSaveLoad
    Left = 58
    Top = 166
  end
  object tblLV: TADOTable
    Connection = cnctnSaveLoad
    Left = 114
    Top = 166
  end
  object tblFP: TADOTable
    Connection = cnctnSaveLoad
    Left = 174
    Top = 164
  end
end
