object fDependence: TfDependence
  Left = 375
  Top = 268
  Width = 482
  Height = 320
  BorderIcons = [biSystemMenu, biMaximize]
  Caption = '����������� ���������'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  Position = poMainFormCenter
  OnCreate = FormCreate
  OnDestroy = FormDestroy
  OnShow = FormShow
  PixelsPerInch = 96
  TextHeight = 13
  object pnl: TPanel
    Left = 0
    Top = 0
    Width = 474
    Height = 85
    Align = alTop
    TabOrder = 0
    object lblV: TLabel
      Left = 6
      Top = 4
      Width = 69
      Height = 13
      AutoSize = False
      Caption = '�����������'
    end
    object lblH: TLabel
      Left = 6
      Top = 44
      Width = 77
      Height = 13
      Caption = '�������������'
    end
    object lblVMeasureUnit: TLabel
      Left = 351
      Top = 4
      Width = 75
      Height = 13
      Anchors = [akTop, akRight]
      Caption = '��. ���������'
    end
    object lblHMeasureUnit: TLabel
      Left = 351
      Top = 44
      Width = 75
      Height = 13
      Anchors = [akTop, akRight]
      Caption = '��. ���������'
    end
    object cmbxV: TComboBox
      Left = 6
      Top = 18
      Width = 341
      Height = 21
      Style = csDropDownList
      Anchors = [akLeft, akTop, akRight]
      ItemHeight = 13
      TabOrder = 0
      OnChange = cmbxVChange
    end
    object cmbxH: TComboBox
      Left = 6
      Top = 58
      Width = 341
      Height = 21
      Style = csDropDownList
      Anchors = [akLeft, akTop, akRight]
      ItemHeight = 13
      TabOrder = 2
      OnChange = cmbxHChange
    end
    object cmbxVMeasureUnit: TComboBox
      Left = 350
      Top = 18
      Width = 120
      Height = 21
      Style = csDropDownList
      Anchors = [akTop, akRight]
      ItemHeight = 13
      TabOrder = 1
      OnChange = cmbxVMeasureUnitChange
      Items.Strings = (
        '�������������'
        '�����������')
    end
    object cmbxHMeasureUnit: TComboBox
      Left = 350
      Top = 58
      Width = 120
      Height = 21
      Style = csDropDownList
      Anchors = [akTop, akRight]
      ItemHeight = 13
      TabOrder = 3
      OnChange = cmbxVMeasureUnitChange
      Items.Strings = (
        '�������������'
        '�����������')
    end
  end
  object chrt: TChart
    Left = 0
    Top = 85
    Width = 474
    Height = 188
    AllowZoom = False
    BackWall.Brush.Color = clWhite
    BackWall.Brush.Style = bsClear
    Title.Text.Strings = (
      '')
    Title.Visible = False
    Legend.Visible = False
    View3D = False
    View3DWalls = False
    Align = alClient
    TabOrder = 1
    OnMouseMove = chrtMouseMove
    object Series1: TPointSeries
      Marks.ArrowLength = 0
      Marks.Visible = False
      SeriesColor = clRed
      Pointer.InflateMargins = True
      Pointer.Style = psRectangle
      Pointer.Visible = True
      XValues.DateTime = False
      XValues.Name = 'X'
      XValues.Multiplier = 1
      XValues.Order = loAscending
      YValues.DateTime = False
      YValues.Name = 'Y'
      YValues.Multiplier = 1
      YValues.Order = loNone
    end
  end
  object StatusBar: TStatusBar
    Left = 0
    Top = 273
    Width = 474
    Height = 20
    Panels = <>
    SimplePanel = True
  end
end
