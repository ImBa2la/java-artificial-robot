object fResult: TfResult
  Left = 421
  Top = 307
  Width = 567
  Height = 352
  BorderIcons = [biSystemMenu, biMaximize]
  Caption = '������ ��������'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  Position = poMainFormCenter
  PixelsPerInch = 96
  TextHeight = 13
  object grdResult: TAdvStringGrid
    Left = 0
    Top = 22
    Width = 559
    Height = 303
    Align = alClient
    ColCount = 4
    DefaultColWidth = 32
    DefaultRowHeight = 21
    FixedCols = 0
    Options = [goFixedVertLine, goFixedHorzLine, goVertLine, goHorzLine, goRangeSelect, goColSizing]
    TabOrder = 0
    Bands.Active = False
    Bands.PrimaryColor = clInfoBk
    Bands.PrimaryLength = 1
    Bands.SecondaryColor = clWindow
    Bands.SecondaryLength = 1
    Bands.Print = False
    AutoNumAlign = True
    AutoSize = True
    VAlignment = vtaTop
    EnhTextSize = False
    EnhRowColMove = False
    SortFixedCols = False
    SortNormalCellsOnly = False
    SizeWithForm = False
    Multilinecells = False
    SortDirection = sdAscending
    SortFull = True
    SortAutoFormat = True
    SortShow = False
    SortIndexShow = False
    EnableGraphics = False
    SortColumn = 0
    HintColor = clYellow
    SelectionColor = clHighlight
    SelectionTextColor = clHighlightText
    SelectionRectangle = False
    SelectionRTFKeep = False
    HintShowCells = False
    OleDropTarget = False
    OleDropSource = False
    OleDropRTF = False
    PrintSettings.FooterSize = 0
    PrintSettings.HeaderSize = 0
    PrintSettings.Time = ppNone
    PrintSettings.Date = ppNone
    PrintSettings.DateFormat = 'dd/mm/yyyy'
    PrintSettings.PageNr = ppNone
    PrintSettings.Title = ppNone
    PrintSettings.Font.Charset = DEFAULT_CHARSET
    PrintSettings.Font.Color = clWindowText
    PrintSettings.Font.Height = -11
    PrintSettings.Font.Name = 'MS Sans Serif'
    PrintSettings.Font.Style = []
    PrintSettings.HeaderFont.Charset = DEFAULT_CHARSET
    PrintSettings.HeaderFont.Color = clWindowText
    PrintSettings.HeaderFont.Height = -11
    PrintSettings.HeaderFont.Name = 'MS Sans Serif'
    PrintSettings.HeaderFont.Style = []
    PrintSettings.FooterFont.Charset = DEFAULT_CHARSET
    PrintSettings.FooterFont.Color = clWindowText
    PrintSettings.FooterFont.Height = -11
    PrintSettings.FooterFont.Name = 'MS Sans Serif'
    PrintSettings.FooterFont.Style = []
    PrintSettings.Borders = pbNoborder
    PrintSettings.BorderStyle = psSolid
    PrintSettings.Centered = False
    PrintSettings.RepeatFixedRows = False
    PrintSettings.RepeatFixedCols = False
    PrintSettings.LeftSize = 0
    PrintSettings.RightSize = 0
    PrintSettings.ColumnSpacing = 0
    PrintSettings.RowSpacing = 0
    PrintSettings.TitleSpacing = 0
    PrintSettings.Orientation = poPortrait
    PrintSettings.FixedWidth = 0
    PrintSettings.FixedHeight = 0
    PrintSettings.UseFixedHeight = False
    PrintSettings.UseFixedWidth = False
    PrintSettings.FitToPage = fpNever
    PrintSettings.PageNumSep = '/'
    PrintSettings.NoAutoSize = False
    PrintSettings.PrintGraphics = False
    HTMLSettings.Width = 100
    Navigation.AllowInsertRow = False
    Navigation.AllowDeleteRow = False
    Navigation.AdvanceOnEnter = False
    Navigation.AdvanceInsert = False
    Navigation.AutoGotoWhenSorted = False
    Navigation.AutoGotoIncremental = False
    Navigation.AutoComboDropSize = False
    Navigation.AdvanceDirection = adLeftRight
    Navigation.AllowClipboardShortCuts = False
    Navigation.AllowSmartClipboard = False
    Navigation.AllowRTFClipboard = False
    Navigation.AdvanceAuto = False
    Navigation.InsertPosition = pInsertBefore
    Navigation.CursorWalkEditor = False
    Navigation.MoveRowOnSort = False
    Navigation.ImproveMaskSel = False
    Navigation.AlwaysEdit = False
    Navigation.CopyHTMLTagsToClipboard = True
    ColumnSize.Save = False
    ColumnSize.Stretch = False
    ColumnSize.Location = clRegistry
    CellNode.Color = clSilver
    CellNode.NodeType = cnFlat
    CellNode.NodeColor = clBlack
    SizeWhileTyping.Height = False
    SizeWhileTyping.Width = False
    MouseActions.AllSelect = False
    MouseActions.ColSelect = False
    MouseActions.RowSelect = False
    MouseActions.DirectEdit = False
    MouseActions.DirectComboDrop = False
    MouseActions.DisjunctRowSelect = False
    MouseActions.AllColumnSize = False
    MouseActions.AllRowSize = False
    MouseActions.CaretPositioning = False
    IntelliPan = ipVertical
    URLColor = clBlue
    URLShow = False
    URLFull = False
    URLEdit = False
    ScrollType = ssNormal
    ScrollColor = clNone
    ScrollWidth = 16
    ScrollProportional = False
    ScrollHints = shNone
    OemConvert = False
    FixedFooters = 0
    FixedRightCols = 0
    FixedColWidth = 136
    FixedRowHeight = 30
    FixedFont.Charset = DEFAULT_CHARSET
    FixedFont.Color = clWindowText
    FixedFont.Height = -11
    FixedFont.Name = 'MS Sans Serif'
    FixedFont.Style = []
    FixedAsButtons = False
    FloatFormat = '%.4f'
    WordWrap = False
    ColumnHeaders.Strings = (
      '�������� ��������'
      '���'
      '��.���')
    Lookup = False
    LookupCaseSensitive = False
    LookupHistory = False
    HideFocusRect = False
    BackGround.Top = 0
    BackGround.Left = 0
    BackGround.Display = bdTile
    Hovering = False
    Filter = <>
    FilterActive = False
    ColWidths = (
      136
      39
      42
      43)
    RowHeights = (
      30
      21
      21
      21
      21)
  end
  object tlb: TToolBar
    Left = 0
    Top = 0
    Width = 559
    Height = 22
    AutoSize = True
    Caption = 'tlbr'
    EdgeInner = esNone
    EdgeOuter = esNone
    Flat = True
    Images = imglst
    TabOrder = 1
    object tlbtnSave: TToolButton
      Left = 0
      Top = 0
      Hint = '���������'
      Caption = '���������'
      ImageIndex = 0
      ParentShowHint = False
      ShowHint = True
      OnClick = tlbtnSaveClick
    end
    object ToolButton1: TToolButton
      Left = 23
      Top = 0
      Hint = '����������� ���������'
      Caption = 'ToolButton1'
      ImageIndex = 1
      ParentShowHint = False
      ShowHint = True
      OnClick = ToolButton1Click
    end
  end
  object imglst: TImageList
    Left = 252
    Top = 14
    Bitmap = {
      494C010102000400040010001000FFFFFFFFFF10FFFFFFFFFFFFFFFF424D3600
      0000000000003600000028000000400000001000000001001000000000000008
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000420042000000000000
      0000000000000000000000000042000000000000000018631863FF7F18631863
      FF7F18631863FF7F18631863FF7F186318630000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000420042000000000000
      0000000000000000000000000042000000000000000000000000FF7F18631863
      FF7F18631863FF7F18631863FF7F186318630000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000420042000000000000
      00000000000000000000000000420000000000000000FF7FFF7F0000FF7FFF7F
      FF7FFF7FFF7FFF7FFF7FFF7FFF7FFF7F00000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000420042000000000000
      0000000000000000000000000042000000000000000018631863000000000000
      FF7F00000000000018631863FF7F000018630000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000420042004200420042
      0042004200420042004200420042000000000000000018631863FF7F18630000
      FF7F00001863FF7F000018630000186318630000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000420042000000000000
      00000000000000000000004200420000000000000000007CFF7FFF7FFF7FFF7F
      0000FF7FFF7FFF7FFF7F00000000FF7FFF7F0000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000420000000000000000
      000000000000000000000000004200000000000000001863007CFF7F18631863
      FF7F007C1863FF7F186318630000186318630000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000420000000000000000
      0000000000000000000000000042000000000000000018631863007C18631863
      007C1863007C007C18631863FF7F186318630000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000420000000000000000
      00000000000000000000000000420000000000000000FF7FFF7F007C007C007C
      FF7FFF7FFF7FFF7F007CFF7F007C007CFF7F0000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000420000000000000000
      0000000000000000000000000042000000000000000018631863FF7F18631863
      FF7F18631863FF7F007C007CFF7F007C18630000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000420000000000000000
      0000000000000000000000000000000000000000000018631863FF7F18631863
      FF7F18631863FF7F18631863FF7F1863007C0000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000420000000000000000
      00000000000000000000000000000000000000000000FF7FFF7FFF7FFF7FFF7F
      FF7FFF7FFF7FFF7FFF7FFF7FFF7FFF7FFF7F0000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000018631863FF7F18631863
      FF7F18631863FF7F18631863FF7F186318630000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000018631863FF7F18631863
      FF7F18631863FF7F18631863FF7F186318630000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      000000000000000000000000000000000000424D3E000000000000003E000000
      2800000040000000100000000100010000000000800000000000000000000000
      000000000000000000000000FFFFFF00FFFFB6DB00000000C001000000000000
      8031800000000000803180000000000080310000000000008001800000000000
      800180000000000080010000000000008FF18000000000008FF1800000000000
      8FF10000000000008FF18000000000008FF18000000000008FF5000000000000
      8001800000000000FFFF80000000000000000000000000000000000000000000
      000000000000}
  end
  object svdlg: TSaveDialog
    DefaultExt = 'xls'
    Filter = 'Microsoft Exel (*.xls)|*.xls'
    Options = [ofOverwritePrompt, ofHideReadOnly, ofEnableSizing]
    Left = 286
    Top = 14
  end
end
