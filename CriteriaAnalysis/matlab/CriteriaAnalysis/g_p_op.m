function out = g_p_op(l, v, u )
    p = log(l)/log(1-l);
    m = length(v);
    
    e1 = @eq1;
    function y = eq1(b)
        y = -1 - m*b;
        for i = 1:m
            y = y + (v(i)+b.^p).^(1/p);
        end
    end
    b = fzero(e1, [0, 10]);
    
    if ndims(u) == 1
        y = -m*b;
        for i = 1:m
            y = y + (v(i)*u(i)+b^p).^(1/p);
        end
        y = y .^ p;
    elseif  ndims(u) == 2
        [n,nn] = size(u);
        y = zeros(n);
        y = y - m*b;
        for i = 1:m
            y(:) = y(:) + (v(i)*u(:,i)+b^p).^(1/p);
        end
        y = y .^ p;
    elseif ndims(u) == 3
        [n,nn, nnn] = size(u);
        y = zeros(n,nn);        
        y = y - m*b;
        for i = 1:m
            y(:,:) = y(:,:) + (v(i)*u(:,:,i)+b^p).^(1/p);
        end
        y = y .^ p;        
    end
    out = y;
end