object frmCriteria: TfrmCriteria
  Left = 0
  Top = 0
  Width = 593
  Height = 370
  AutoSize = True
  TabOrder = 0
  object pnl: TPanel
    Left = 0
    Top = 0
    Width = 593
    Height = 370
    Align = alClient
    BevelOuter = bvNone
    BorderWidth = 2
    TabOrder = 0
    object trvw: TTreeView
      Left = 2
      Top = 2
      Width = 208
      Height = 366
      Align = alClient
      HideSelection = False
      Images = imglst
      Indent = 19
      ReadOnly = True
      ShowRoot = False
      TabOrder = 0
      OnChange = OnNodeSelection
      OnCollapsing = OnNodeCollapsing
      OnDeletion = OnNodeDeletion
    end
    object pnlPropertes: TPanel
      Left = 210
      Top = 2
      Width = 381
      Height = 366
      Align = alRight
      BevelOuter = bvNone
      TabOrder = 1
      object pgcntrl: TPageControl
        Left = 0
        Top = 0
        Width = 381
        Height = 337
        ActivePage = tbshtSCProperties
        Align = alClient
        MultiLine = True
        TabOrder = 0
        object tbshtCCProperties: TTabSheet
          BorderWidth = 2
          Caption = 'Общие'
          object lblCCName: TLabel
            Left = 84
            Top = 4
            Width = 50
            Height = 13
            Caption = 'Название'
          end
          object lblCCWeight: TLabel
            Left = 116
            Top = 28
            Width = 19
            Height = 13
            Caption = 'Вес'
          end
          object lblCCOperator: TLabel
            Left = 6
            Top = 54
            Width = 128
            Height = 13
            Caption = 'Оператор агрегирования'
          end
          object lblLambda: TLabel
            Left = 8
            Top = 78
            Width = 127
            Height = 13
            Caption = 'Коэффициент жесткости'
          end
          object edtCCriteriaName: TEdit
            Left = 144
            Top = 0
            Width = 225
            Height = 21
            MaxLength = 35
            TabOrder = 0
            OnChange = OnCCNameChange
          end
          object edtCWeight: TEdit
            Left = 144
            Top = 24
            Width = 121
            Height = 21
            MaxLength = 5
            TabOrder = 1
            OnChange = OnCCWeightChange
          end
          object cmbxAOperator: TComboBox
            Left = 144
            Top = 50
            Width = 225
            Height = 21
            Style = csDropDownList
            Font.Charset = RUSSIAN_CHARSET
            Font.Color = clWindowText
            Font.Height = -11
            Font.Name = 'MS Sans Serif'
            Font.Style = []
            ItemHeight = 13
            ParentFont = False
            TabOrder = 2
            OnChange = OnCCOperatorChange
            Items.Strings = (
              'аддитивный'
              'степенной I-типа'
              'степенной II-типа'
              'двойной степенной'
              'мультипликативный')
          end
          object edtLambda: TEdit
            Left = 144
            Top = 74
            Width = 121
            Height = 21
            TabOrder = 3
            OnChange = OnCCLambdaChange
          end
        end
        object tbshtSCProperties: TTabSheet
          BorderWidth = 2
          Caption = 'Общие'
          ImageIndex = 1
          object lblSCName: TLabel
            Left = 84
            Top = 4
            Width = 50
            Height = 13
            Caption = 'Название'
          end
          object lblSCWeight: TLabel
            Left = 262
            Top = 28
            Width = 19
            Height = 13
            Caption = 'Вес'
          end
          object lblSCFBehavior: TLabel
            Left = 82
            Top = 54
            Width = 56
            Height = 13
            Caption = 'Поведение'
          end
          object lblDataField: TLabel
            Left = 94
            Top = 28
            Width = 41
            Height = 13
            Caption = 'Данные'
          end
          object edtSCriteriaName: TEdit
            Left = 142
            Top = 0
            Width = 225
            Height = 21
            MaxLength = 35
            TabOrder = 0
            OnChange = OnSCNameChange
          end
          object edtSWeight: TEdit
            Left = 284
            Top = 25
            Width = 83
            Height = 21
            MaxLength = 5
            TabOrder = 2
            OnChange = OnSCWeightChange
          end
          object cmbxFBehavior: TComboBox
            Left = 142
            Top = 50
            Width = 225
            Height = 21
            Style = csDropDownList
            ItemHeight = 13
            TabOrder = 3
            OnChange = OnBehaviorChange
            Items.Strings = (
              'возрастающее'
              'убывающее'
              'качественное')
          end
          object cmbxDataField: TComboBox
            Left = 142
            Top = 25
            Width = 113
            Height = 21
            Style = csDropDownList
            Color = clInfoBk
            ItemHeight = 13
            TabOrder = 1
            OnChange = cmbxDataFieldChange
            Items.Strings = (
              'не определены')
          end
          object pnl1: TPanel
            Left = 0
            Top = 74
            Width = 369
            Height = 213
            Align = alBottom
            Anchors = [akLeft, akTop, akRight, akBottom]
            BevelOuter = bvNone
            TabOrder = 4
            inline frmSCValues: TfrmSCValues
              Width = 369
              Height = 213
              Align = alClient
              TabOrder = 1
              inherited Label1: TLabel
                Left = 77
              end
              inherited strgrdValue: TStringGrid
                Left = 144
                Height = 181
                ParentShowHint = False
                ShowHint = True
                OnDblClick = frmSCValuesstrgrdValueDblClick
                OnKeyDown = OnStrgrdValueKeyDown
                OnSelectCell = frmSCValuesstrgrdValueSelectCell
              end
              inherited pnl: TPanel
                Top = 181
                Width = 369
                inherited btnFill: TButton
                  Left = 292
                  OnClick = frmSCValuesbtnFillClick
                end
              end
            end
            inline frmSCCnvrsnPrmtr: TfrmSCCnvrsnPrmtr
              Width = 369
              Height = 213
              Align = alClient
              inherited lblMin: TLabel
                Left = 119
              end
              inherited lblSCFunction: TLabel
                Left = 41
                Top = 32
              end
              inherited lblMax: TLabel
                Left = 253
              end
              inherited edtSMin: TEdit
                Left = 142
                Top = 2
                OnChange = OnSCMinChange
              end
              inherited cmbxFunction: TComboBox
                Left = 142
                Top = 28
                OnChange = OnSCConvFunctionChange
                Items.Strings = (
                  'линейная'
                  'бета-распределения'
                  'гауссовская'
                  'бета'
                  'показательная')
              end
              inherited edtSMax: TEdit
                Left = 280
                Top = 2
                Width = 86
                OnChange = OnSCMaxChange
              end
            end
          end
        end
        object tbshtComment: TTabSheet
          BorderWidth = 2
          Caption = 'Описание'
          ImageIndex = 2
          object rchdtComment: TRichEdit
            Left = 0
            Top = 0
            Width = 369
            Height = 287
            Align = alClient
            TabOrder = 0
            OnChange = OnCommentChange
            OnExit = OnCommentExit
          end
        end
        object tbshtConvFunction: TTabSheet
          BorderWidth = 2
          Caption = 'Функция перевода'
          ImageIndex = 3
          OnShow = OnFuncTabSheetShow
          object chrt: TChart
            Left = 0
            Top = 89
            Width = 369
            Height = 198
            AllowZoom = False
            BackWall.Brush.Color = clWhite
            BackWall.Brush.Style = bsClear
            BottomWall.Pen.Visible = False
            Gradient.EndColor = clWhite
            LeftWall.Brush.Color = clWhite
            LeftWall.Brush.Style = bsClear
            Title.Text.Strings = (
              '')
            Title.Visible = False
            BottomAxis.LabelsSeparation = 40
            BottomAxis.StartPosition = 3
            BottomAxis.EndPosition = 97
            Chart3DPercent = 55
            LeftAxis.ExactDateTime = False
            LeftAxis.Increment = 0.1
            LeftAxis.StartPosition = 3
            LeftAxis.EndPosition = 96
            Legend.Frame.Visible = False
            Legend.Visible = False
            RightAxis.Visible = False
            TopAxis.Axis.Visible = False
            TopAxis.Grid.Visible = False
            TopAxis.Labels = False
            TopAxis.MinorTicks.Visible = False
            TopAxis.Ticks.Visible = False
            TopAxis.TicksInner.Visible = False
            TopAxis.Visible = False
            View3D = False
            View3DWalls = False
            Align = alClient
            BevelOuter = bvLowered
            BevelWidth = 0
            BorderStyle = bsSingle
            Color = clWhite
            TabOrder = 0
            object Series: TLineSeries
              Marks.ArrowLength = 8
              Marks.Visible = False
              SeriesColor = clRed
              Title = 'ConvFunc'
              Pointer.InflateMargins = True
              Pointer.Style = psRectangle
              Pointer.Visible = False
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
          object pnlForFuncFrame: TPanel
            Left = 0
            Top = 0
            Width = 369
            Height = 89
            Align = alTop
            BevelOuter = bvNone
            TabOrder = 1
            inline frmLinerFunc: TfrmLinerFunc
              Width = 369
              Height = 89
              Align = alClient
              inherited edtLSatiation: TEdit
                OnChange = frmLinerFuncedtLSatiationChange
              end
              inherited edtRSatiation: TEdit
                OnChange = frmLinerFuncedtRSatiationChange
              end
            end
            inline frmGaussianFunc: TfrmGaussianFunc
              Width = 369
              Height = 89
              Align = alClient
              TabOrder = 4
              inherited edtLSatiation: TEdit
                OnChange = frmGaussianFuncedtLSatiationChange
              end
              inherited edtRSatiation: TEdit
                OnChange = frmGaussianFuncedtRSatiationChange
              end
            end
            inline frmBetaDistributionFunc: TfrmBetaDistributionFunc
              Width = 369
              Height = 89
              Align = alClient
              TabOrder = 1
              inherited edtP: TEdit
                OnChange = OnSCPChange
              end
              inherited edtQ: TEdit
                OnChange = OnSCQChange
              end
              inherited edtLSatiation: TEdit
                OnChange = frmBetaDistributionFuncedtLSatiationChange
              end
              inherited UpDownLSatiation: TUpDown
                Max = 100
              end
              inherited edtRSatiation: TEdit
                OnChange = frmBetaDistributionFuncedtRSatiationChange
              end
            end
            inline frmBetaFunc: TfrmBetaFunc
              Width = 369
              Height = 89
              Align = alClient
              TabOrder = 2
              inherited edtP: TEdit
                OnChange = frmBetaFuncedtPChange
              end
              inherited edtQ: TEdit
                OnChange = frmBetaFuncedtQChange
              end
              inherited edtLSatiation: TEdit
                OnChange = frmBetaFuncedtLSatiationChange
              end
              inherited edtRSatiation: TEdit
                OnChange = frmBetaFuncedtRSatiationChange
              end
            end
            inline frmExponentialFunc: TfrmExponentialFunc
              Width = 369
              Height = 89
              Align = alClient
              TabOrder = 3
              inherited edtBase: TEdit
                OnChange = frmExponentialFuncedtBaseChange
              end
              inherited edtLSatiation: TEdit
                OnChange = frmExponentialFuncedtLSatiationChange
              end
              inherited edtRSatiation: TEdit
                OnChange = frmExponentialFuncedtRSatiationChange
              end
            end
          end
        end
        object tbshtAOperator: TTabSheet
          Caption = 'Оператор агрегирования'
          ImageIndex = 4
          OnShow = tbshtAOperatorShow
          object chrtAOperator: TChart
            Left = 0
            Top = 0
            Width = 373
            Height = 291
            AllowZoom = False
            BackWall.Brush.Color = clWhite
            BackWall.Brush.Style = bsClear
            BottomWall.Pen.Visible = False
            Gradient.EndColor = clWhite
            LeftWall.Brush.Color = clWhite
            LeftWall.Brush.Style = bsClear
            Title.Text.Strings = (
              '')
            Title.Visible = False
            BottomAxis.Automatic = False
            BottomAxis.AutomaticMaximum = False
            BottomAxis.AutomaticMinimum = False
            BottomAxis.ExactDateTime = False
            BottomAxis.Increment = 0.1
            BottomAxis.LabelsSeparation = 40
            BottomAxis.Maximum = 1
            BottomAxis.StartPosition = 3
            BottomAxis.EndPosition = 97
            Chart3DPercent = 55
            LeftAxis.ExactDateTime = False
            LeftAxis.Increment = 0.1
            LeftAxis.StartPosition = 3
            LeftAxis.EndPosition = 96
            Legend.Font.Charset = RUSSIAN_CHARSET
            Legend.Font.Color = clBlack
            Legend.Font.Height = -11
            Legend.Font.Name = 'Arial'
            Legend.Font.Style = []
            Legend.Frame.Visible = False
            RightAxis.Visible = False
            TopAxis.Axis.Visible = False
            TopAxis.Grid.Visible = False
            TopAxis.Labels = False
            TopAxis.MinorTicks.Visible = False
            TopAxis.Ticks.Visible = False
            TopAxis.TicksInner.Visible = False
            TopAxis.Visible = False
            View3D = False
            View3DWalls = False
            Align = alClient
            BevelOuter = bvLowered
            BevelWidth = 0
            BorderStyle = bsSingle
            Color = clWhite
            TabOrder = 0
            object SeriesF: TLineSeries
              Marks.ArrowLength = 8
              Marks.Font.Charset = DEFAULT_CHARSET
              Marks.Font.Color = clMaroon
              Marks.Font.Height = -11
              Marks.Font.Name = 'Arial'
              Marks.Font.Style = []
              Marks.Visible = False
              SeriesColor = clRed
              Title = 'F функция'
              Pointer.InflateMargins = True
              Pointer.Style = psRectangle
              Pointer.Visible = False
              XValues.DateTime = False
              XValues.Name = 'X'
              XValues.Multiplier = 1
              XValues.Order = loAscending
              YValues.DateTime = False
              YValues.Name = 'Y'
              YValues.Multiplier = 1
              YValues.Order = loNone
            end
            object SeriesG: TLineSeries
              Marks.ArrowLength = 8
              Marks.Visible = False
              SeriesColor = clGreen
              Title = 'G функция'
              Pointer.InflateMargins = True
              Pointer.Style = psRectangle
              Pointer.Visible = False
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
        end
      end
      object pnlOKCancelBtn: TPanel
        Left = 0
        Top = 337
        Width = 381
        Height = 29
        Align = alBottom
        BevelOuter = bvNone
        TabOrder = 1
        object btnApply: TButton
          Left = 226
          Top = 4
          Width = 75
          Height = 25
          Caption = 'Применить'
          Enabled = False
          TabOrder = 0
          OnClick = ApplyChanges
        end
        object btnCancel: TButton
          Left = 306
          Top = 4
          Width = 75
          Height = 25
          Caption = 'Отмена'
          Enabled = False
          TabOrder = 1
          OnClick = CancelChanges
        end
      end
    end
  end
  object imglst: TImageList
    Left = 36
    Top = 22
    Bitmap = {
      494C010102000400040010001000FFFFFFFFFF10FFFFFFFFFFFFFFFF424D3600
      0000000000003600000028000000400000001000000001001000000000000008
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000007C007C007C007C007C007C
      00000000007C007C007C007C007C007C00000000000200020002000200020002
      0000000000020002000200020002000200000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000007C007C007C007C007C007C
      00000000007C007C007C007C007C007C00000000000200020002000200020002
      0000000000020002000200020002000200000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000007C007C007C007C007C007C
      00000000007C007C007C007C007C007C00000000000200020002000200020002
      0000000000020002000200020002000200000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000007C007C007C007C007C007C
      00000000007C007C007C007C007C007C00000000000200020002000200020002
      0000000000020002000200020002000200000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      000000000000000000000000000000000000000000000000007C007C00000000
      0000000000000000007C007C0000000000000000000000000002000200000000
      0000000000000000000200020000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000007C007C0000
      000000000000007C007C00000000000000000000000000000000000200020000
      0000000000000002000200000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      00000000000000000000000000000000000000000000000000000000007C007C
      00000000007C007C000000000000000000000000000000000000000000020002
      0000000000020002000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      000000000000000000000000000000000000000000000000000000000000007C
      007C007C007C0000000000000000000000000000000000000000000000000002
      0002000200020000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      007C007C00000000000000000000000000000000000000000000000000000000
      0002000200000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      007C007C00000000000000000000000000000000000000000000000000000000
      0002000200000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      00000000000000000000000000000000000000000000000000000000007C007C
      007C007C007C007C000000000000000000000000000000000000000000020002
      0002000200020002000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      00000000000000000000000000000000000000000000000000000000007C007C
      007C007C007C007C000000000000000000000000000000000000000000020002
      0002000200020002000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      00000000000000000000000000000000000000000000000000000000007C007C
      007C007C007C007C000000000000000000000000000000000000000000020002
      0002000200020002000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      00000000000000000000000000000000000000000000000000000000007C007C
      007C007C007C007C000000000000000000000000000000000000000000020002
      0002000200020002000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      0000000000000000000000000000000000000000000000000000000000000000
      000000000000000000000000000000000000424D3E000000000000003E000000
      2800000040000000100000000100010000000000800000000000000000000000
      000000000000000000000000FFFFFF00FFFFFFFF000000008181818100000000
      818181810000000081818181000000008181818100000000E7E7E7E700000000
      F3CFF3CF00000000F99FF99F00000000FC3FFC3F00000000FE7FFE7F00000000
      FE7FFE7F00000000F81FF81F00000000F81FF81F00000000F81FF81F00000000
      F81FF81F00000000FFFFFFFF0000000000000000000000000000000000000000
      000000000000}
  end
end
