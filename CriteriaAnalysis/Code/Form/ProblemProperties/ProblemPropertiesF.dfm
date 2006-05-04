object fProblemProperties: TfProblemProperties
  Left = 370
  Top = 282
  Width = 418
  Height = 204
  BorderIcons = [biSystemMenu]
  BorderStyle = bsSizeToolWin
  BorderWidth = 4
  Caption = 'Свойства задачи'
  Color = clBtnFace
  Constraints.MinHeight = 204
  Constraints.MinWidth = 418
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  Position = poMainFormCenter
  OnShow = FormShow
  PixelsPerInch = 96
  TextHeight = 13
  object btbtnCancel: TBitBtn
    Left = 336
    Top = 112
    Width = 65
    Height = 25
    Anchors = [akRight, akBottom]
    Cancel = True
    Caption = 'Отмена'
    ModalResult = 2
    TabOrder = 2
    NumGlyphs = 2
  end
  object btbtnApply: TBitBtn
    Left = 336
    Top = 144
    Width = 65
    Height = 25
    Anchors = [akRight, akBottom]
    Caption = 'Применить'
    Default = True
    Enabled = False
    TabOrder = 3
    OnClick = btbtnApplyClick
    NumGlyphs = 2
  end
  object btbtnOK: TBitBtn
    Left = 336
    Top = 80
    Width = 65
    Height = 25
    Anchors = [akRight, akBottom]
    Caption = 'OK'
    ModalResult = 1
    TabOrder = 1
    OnClick = btbtnOKClick
    NumGlyphs = 2
  end
  object pgcntrl: TPageControl
    Left = 0
    Top = 0
    Width = 329
    Height = 169
    ActivePage = tbshtCommon
    Align = alLeft
    Anchors = [akLeft, akTop, akRight, akBottom]
    TabOrder = 0
    object tbshtCommon: TTabSheet
      Caption = 'Общие'
      object lblProblemName: TLabel
        Left = 8
        Top = 7
        Width = 50
        Height = 13
        Caption = 'Название'
        Font.Charset = DEFAULT_CHARSET
        Font.Color = clWindowText
        Font.Height = -11
        Font.Name = 'MS Sans Serif'
        Font.Style = []
        ParentFont = False
      end
      object lblAuthor: TLabel
        Left = 28
        Top = 30
        Width = 30
        Height = 13
        Caption = 'Автор'
      end
      object lblDescription: TLabel
        Left = 8
        Top = 60
        Width = 50
        Height = 13
        Caption = 'Описание'
      end
      object mDescription: TRichEdit
        Left = 64
        Top = 64
        Width = 253
        Height = 73
        Anchors = [akLeft, akTop, akRight, akBottom]
        MaxLength = 256
        ScrollBars = ssBoth
        TabOrder = 2
        WordWrap = False
        OnChange = EnableApplyBtn
        OnKeyDown = mDescriptionKeyDown
      end
      object edtProblemName: TEdit
        Left = 64
        Top = 4
        Width = 253
        Height = 21
        Anchors = [akLeft, akTop, akRight]
        MaxLength = 35
        TabOrder = 0
        OnChange = EnableApplyBtn
      end
      object edtAuthor: TEdit
        Left = 64
        Top = 28
        Width = 253
        Height = 21
        Anchors = [akLeft, akTop, akRight]
        MaxLength = 35
        TabOrder = 1
        OnChange = EnableApplyBtn
      end
    end
  end
end
